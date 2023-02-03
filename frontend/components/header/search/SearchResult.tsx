import {infoScreenState} from "@atoms/InfoScreenAtom";
import {animeState} from "@atoms/VideoPlayerAtom";
import AnimeService from "@consumet/AnimeService";
import AddToLibraryIcon from "@icons/AddToLibraryIcon";
import LikeButtonIcon from "@icons/LikeButtonIcon";
import {Anime, hasAllAnimeProperties} from "@interfaces/Anime";
import {RecentEpisode} from "@interfaces/RecentEpisode";
import {LOGGER} from "@util/Logger";
import Image from "next/image";
import React from "react";
import {useSetRecoilState} from "recoil";

interface Props {
    anime: Anime;
}

export default function SearchResult({anime}: Props) {
    const setShowInfoScreen = useSetRecoilState(infoScreenState)
    const setCurrentAnime = useSetRecoilState(animeState)
    const title = anime.title.english ? anime.title.english : anime.title.romaji
    
    const handleClickedAnime = async () => {
        const id: string = anime.id.toString();
        if (hasAllAnimeProperties(anime)) {
            setCurrentAnime(anime);
            setShowInfoScreen(true);
        } else {
            const details: Anime = await AnimeService.getAnimeDetails(id);
            setCurrentAnime(details);
            setShowInfoScreen(true);
        }
    }
    
    return (
        <div className="min-w-[220px] max-w-[250px] h-full overflow-hidden" onClick={() => handleClickedAnime().catch(() => {
            LOGGER.error("Error occurred when trying to handle clicked anime")
        })}>
            <div className={"rounded-md cursor-pointer overflow-hidden bg-[#1a1920] flex flex-col h-full"}>
                <Image
                    className="relative bg-center bg-no-repeat anime-img-gradient h-[325px]"
                    src={anime.image}
                    alt={title}
                    width={460}
                    height={650}
                />
                
                <div className="font-poppins text-[#d2d2d2] px-3 space-y-2 h-full flex flex-col mb-4">
                    <div className={"flex flex-row justify-between space-x-4 items-center objects-center"}>
                        <ul className="flex flex-row space-x-1 text-xs font-bold">
                            <li className={"text-[#46d369]"}>{anime.rating ? anime.rating : "0"}% match</li>
                            <li className={"text-[#666666]"}> - {anime.status}</li>
                        </ul>
                        <div className={"flex justify-evenly space-x-2"}>
                            <AddToLibraryIcon iconClassName={"h-4 w-4"} buttonClassName={"h-7 w-7"}/>
                            <LikeButtonIcon iconClassName={"h-4 w-4"} buttonClassName={"h-7 w-7"}/>
                        </div>
                    </div>
                    <h1 className={"text-lg font-bold"}>
                        {title.length > 20 ? title.substring(0, 20) + "..." : title}
                    </h1>
                    <p className={"font-light font-poppins text-sm"}> {anime.totalEpisodes} Episodes</p>
                </div>
            
            </div>
        </div>
    )
}
