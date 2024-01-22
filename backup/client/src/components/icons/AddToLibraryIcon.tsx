import useIsSavedToLibrary from "@hooks/useIsSavedToLibrary";
import LibraryService from "@service/LibraryService";
import {LOGGER} from "@util/Logger";

import React from "react";

import {CheckIcon, PlusIcon} from "@heroicons/react/24/solid";
import {animated, useSpring} from "react-spring";


interface Props {
    anime_id: string;
    buttonClassName?: string;
    iconClassName?: string;
}

export default function AddToLibraryIcon({anime_id, buttonClassName, iconClassName}: Props) {
    const {data: isSavedToLibrary, refetch} = useIsSavedToLibrary(anime_id);

    const Icon = isSavedToLibrary ? CheckIcon : PlusIcon;
    const {x} = useSpring({
        from: {x: 0},
        x: isSavedToLibrary ? 1 : 0,
        config: {duration: 500},
    })

    async function handleAddToLibrary(animeId: string) {
        LOGGER.debug(`[AddToLibraryIcon] ${isSavedToLibrary ? "Removing" : "Adding"} anime with id ${animeId} to library`);
        isSavedToLibrary
            ? await LibraryService.removeAnimeFromLibrary(animeId)
            : await LibraryService.addAnimeToLibrary(animeId);
        await refetch();
    }

    return (
        <button
            className={`${buttonClassName ? buttonClassName : ""} ${isSavedToLibrary && "hover:border-green"} defaultButtonRound`}
            onClick={() => handleAddToLibrary(anime_id)}>
            <animated.div style={{
                color: isSavedToLibrary ? "#46d369" : "#d2d2d2",
                scale: x.to({range: [0, 0.25, 0.5, 0.75, 1], output: [1, 0, 1, 1.25, 1]})
            }}>
                <Icon className={`${iconClassName ? iconClassName : ""}`}/>
            </animated.div>
        </button>
    );
}
