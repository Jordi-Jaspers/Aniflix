

import {infoScreenState} from "@atoms/InfoScreenAtom";
import {animeState} from "@atoms/VideoPlayerAtom";
import AnimeService from "@consumet/AnimeService";
import AddToLibraryIcon from "@icons/AddToLibraryIcon";
import LikeButtonIcon from "@icons/LikeButtonIcon";
import type {Anime} from "@interfaces/Anime";
import {LOGGER} from "@util/Logger";

import React from "react";

import Image from "next/image";
import {useSetRecoilState} from "recoil";


interface Props {
    id: string;
    title: string;
    image: string;
    status: string;
    rating: number;
    totalEpisodes: number;
    width: number;
}

export default function ResultCard({id, title, image, status, rating, totalEpisodes, width}: Props) {
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
        <div className={`min-w-[${width - 30}px] max-w-[${width - 15}px] h-full overflow-hidden`}>
            <div className={"rounded-md overflow-hidden bg-[#1a1920] flex flex-col h-full"}>
                <Image
                    className={`aspect-[460/650] bg-center bg-no-repeat anime-img-gradient mb-2 cursor-pointer`}
                    src={image}
                    alt={title}
                    width={460}
                    height={650}
                    onClick={() => handleClickedAnime().catch(() => {
                        LOGGER.error("Error occurred when trying to handle clicked anime")
                    })}
                />
                
                <div className={`font-poppins text-[#d2d2d2] px-3 space-y-2 h-fit flex flex-col`}>
                    <div className={`${width > 390 ? "flex-row" : "flex-col"} flex justify-between pb-0.5`}>
                        <ul className={`${width > 390 ? "text-sm" : "text-xs"} flex flex-row space-x-1 font-bold`}>
                            <li className={"text-[#46d369]"}>{rating ? rating : "0"}% match</li>
                            <li className={"text-[#666666]"}> - {status}</li>
                        </ul>
                        <p className={`${width > 390 ? "text-sm" : "text-xs"} font-light font-poppins`}> {totalEpisodes} Episodes</p>
                    </div>
                    <h1 className={`${width > 390 ? "text-lg" : "text-md"} font-bold h-[48px] overflow-hidden w-fit`}>
                        {title.length > 70 ? getUniformTitle(title.substring(0, 70)) + "..." : getUniformTitle(title)}
                    </h1>
                    <div className={"flex justify-evenly space-x-2 px-2 py-2 pb-4"}>
                        <AddToLibraryIcon anime_id={id} iconClassName={"h-6 w-6"} buttonClassName={"h-10 w-10"}/>
                        <LikeButtonIcon iconClassName={"h-6 w-6"} buttonClassName={"h-10 w-10"}/>
                    </div>
                </div>
            </div>
        </div>
    )
}
