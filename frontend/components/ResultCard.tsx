import {infoScreenState} from "@atoms/InfoScreenAtom";
import {animeState} from "@atoms/VideoPlayerAtom";
import AnimeService from "@consumet/AnimeService";
import AddToLibraryIcon from "@icons/AddToLibraryIcon";
import LikeButtonIcon from "@icons/LikeButtonIcon";
import {Anime} from "@interfaces/Anime";
import {LOGGER} from "@util/Logger";
import Image from "next/image";
import React from "react";
import {useSetRecoilState} from "recoil";

interface Props {
    id: string;
    title: string;
    image: string;
    status: string;
    rating: number;
    totalEpisodes: number;
}

export default function ResultCard({id, title, image, status, rating, totalEpisodes}: Props) {
    const setShowInfoScreen = useSetRecoilState(infoScreenState)
    const setCurrentAnime = useSetRecoilState(animeState)
    const getUniformTitle = (title: string) => {
        return title.toLowerCase().replace("_", " ").replace(/\b[a-z]/g, (letter) => letter.toUpperCase())
    }
    
    const handleClickedAnime = async () => {
        const details: Anime = await AnimeService.getAnimeDetails(id);
        setCurrentAnime(details);
        setShowInfoScreen(true);
    }
    
    return (
        <div className="min-w-[165px] max-w-[190px] h-full overflow-hidden">
            <div className={"rounded-md overflow-hidden bg-[#1a1920] flex flex-col h-full"}>
                <Image
                    className="relative bg-center bg-no-repeat anime-img-gradient h-[260px] mb-2 cursor-pointer"
                    src={image}
                    alt={title}
                    width={460}
                    height={650}
                    onClick={() => handleClickedAnime().catch(() => {
                        LOGGER.error("Error occurred when trying to handle clicked anime")
                    })}
                />
                
                <div className="font-poppins text-[#d2d2d2] px-3 space-y-2 h-fit flex flex-col">
                    <div className={"flex flex-col space-y-2"}>
                        <ul className="flex flex-row space-x-1 text-xs font-bold">
                            <li className={"text-[#46d369]"}>{rating ? rating : "0"}% match</li>
                            <li className={"text-[#666666]"}> - {status}</li>
                        </ul>
                    </div>
                    <h1 className={"text-md font-bold h-[48px] overflow-hidden w-fit"}>
                        {title.length > 35 ? getUniformTitle(title.substring(0, 35)) + "..." : getUniformTitle(title)}
                    </h1>
                    <p className={"font-light font-poppins text-xs"}> {totalEpisodes} Episodes</p>
                    <div className={"flex justify-evenly space-x-2 px-2 py-2 pb-4"}>
                        <AddToLibraryIcon anime_id={id} iconClassName={"h-4 w-4"} buttonClassName={"h-8 w-8"}/>
                        <LikeButtonIcon iconClassName={"h-4 w-4"} buttonClassName={"h-8 w-8"}/>
                    </div>
                </div>
            </div>
        </div>
    )
}
