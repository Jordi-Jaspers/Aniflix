import {infoScreenState} from "@atoms/InfoScreenAtom";
import {animeState} from "@atoms/VideoPlayerAtom";
import AnimeService from "@consumet/AnimeService";
import type {Anime} from "@interfaces/Anime";
import {hasAllAnimeProperties} from "@interfaces/Anime";
import type {RecentEpisode} from "@interfaces/RecentEpisode";
import Image from "next/image";
import {useRouter} from "next/router";

import React from 'react';
import {useSetRecoilState} from 'recoil'

interface Props {
    anime: Anime | RecentEpisode;
    priority?: boolean;
}

export default function Thumbnail({anime, priority}: Props) {
    const router = useRouter()
    const setShowInfoScreen = useSetRecoilState(infoScreenState)
    const setCurrentAnime = useSetRecoilState(animeState)
    
    const videoTitle = (): string => {
        const title: string = anime.title.romaji ? anime.title.romaji : anime.title.english;
        return anime.hasOwnProperty('episodeNumber') ? title + " (Episode " + anime.episodeNumber + ")" : title;
    };
    
    const handleClickedAnime = async () => {
        if (anime.hasOwnProperty('episodeId') && anime.hasOwnProperty('id')) {
            const episode: RecentEpisode = anime as RecentEpisode;
            await router.push('/watch/[anime_id]/[episode_id]', `/watch/${episode.id}/${episode.episodeNumber}`)
        } else {
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
    }
    
    return (
        <div
            className={`relative aspect-square overflow-hidden min-w-[30%] md:min-w-[20%] xl:min-w-[15%] rounded cursor-pointer hover:scale-105 transition duration-200 ease-out`}
            onClick={() => handleClickedAnime()}>
                <div className={"flex flex-col justify-end h-full w-full"}>
                    <div className="absolute top-0 left-0 w-full h-full bg-gradient-to-t from-black/80 via-black/50 to-transparent z-10"/>
                    <p className="p-2 font-poppins w-full leading-none text-xs md:text-sm text-[#e5e5e5] font-bold z-20">{videoTitle()}</p>
                    <Image
                        src={anime.image}
                        alt={videoTitle()}
                        className="absolute object-cover h-full -z-10"
                        height={2160}
                        width={3840}
                        priority={priority}
                    />
                </div>
        </div>
    )
}
