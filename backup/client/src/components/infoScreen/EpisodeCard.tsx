import {infoScreenState} from "@atoms/InfoScreenAtom";
import {animeState} from "@atoms/VideoPlayerAtom";
import HoveringPlayIcon from "@icons/HoveringPlayIcon";
import type {Episode} from "@interfaces/Episode";

import React from "react";

import Image from "next/image";
import router from "next/router";
import {useRecoilValue, useSetRecoilState} from "recoil";


interface Props {
    episode: Episode
}

export default function EpisodeCard({episode}: Props) {
    const date = new Date(episode.airDate);
    const airDate = date.toLocaleDateString();

    const setShowInfoScreen = useSetRecoilState(infoScreenState)
    const anime = useRecoilValue(animeState);

    const handleClickedEpisode = async () => {
        setShowInfoScreen(false);
        await router.push('/watch/[anime_id]/[episode_id]', `/watch/${anime?.id}/${episode.number}`)
    }

    return (
        <div className={"flex justify-center"} onClick={() => handleClickedEpisode()}>
            <div
                className={"border-y-[1px] border-[#404040] min-h-[6em] px-6 rounded w-full flex py-8 cursor-pointer hover:bg-[#1E1E25]"}>
                <div
                    className={"hidden sm:block w-[5%] pr-6 text-[#d2d2d2] text-2xl min-w-fit h-fit font-poppins text-center self-center"}>
                    {episode?.number}
                </div>
                <div
                    className={"relative flex justify-center items-center overflow-hidden w-[40%] sm:w-[20%] rounded-xl"}>
                    <Image
                        src={episode.image}
                        alt={episode.title}
                        className={"object-cover rounded-xl w-full aspect-video"}
                        width={2000}
                        height={2000}
                    />
                    <HoveringPlayIcon/>
                </div>
                <div className={"w-[60%] sm:w-[75%]"}>
                    <div className={"pl-6 h-full"}>
                        <div
                            className={"relative flex h-full flex-col justify-center sm:h-fit sm:flex-row sm:justify-between pb-2 space-x-4"}>
                            <p className={"text-xs font-semibold text-white sm:text-lg sm:font-bold font-poppins"}>
                                {episode.title}
                            </p>
                            <div className={"hidden sm:block text-white font-bold font-poppins"}>
                                {airDate}
                            </div>
                        </div>
                        <p className={"hidden sm:block text-[#d2d2d2] font-poppins font-light text-sm text-justify"}>
                            {episode.description}
                        </p>
                    </div>
                    <p></p>
                </div>
            </div>
        </div>
    )
}
