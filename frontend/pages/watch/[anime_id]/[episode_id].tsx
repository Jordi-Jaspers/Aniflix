import {animeState, episodeState, streamingLinksState} from "@atoms/VideoPlayerAtom";
import VideoControls from "@components/watch/controls/VideoControls";
import {Anime} from "@interfaces/Anime";
import {Episode} from "@interfaces/Episode";
import {MediaSources} from "@interfaces/MediaSources";
import AnimeService from "@util/consumet/AnimeService";
import React, {useEffect, useState} from "react";
import {useSetRecoilState} from "recoil";

interface Props {
    anime: Anime;
    episodeInfo: Episode;
    streamingSources: MediaSources;
}

export default function WatchAnime({anime, episodeInfo, streamingSources}: Props) {
    const [renderControls, setRenderControls] = useState<boolean>(false);
    const setEpisodeState = useSetRecoilState(episodeState);
    const setStreamingLinksState = useSetRecoilState(streamingLinksState);
    const setAnimeState = useSetRecoilState(animeState);
    const [isIdle, setIsIdle] = useState<boolean>(false);
    
    useEffect(() => {
        setEpisodeState(episodeInfo);
        setStreamingLinksState(streamingSources);
        setAnimeState(anime);
        if (anime && episodeInfo && streamingSources) setRenderControls(true);
    }, [episodeInfo, streamingSources, anime]);
    
    useEffect(() => {
        let timeoutId: NodeJS.Timeout;
        const handleMouseMove = () => {
            clearTimeout(timeoutId);
            setIsIdle(false);
            timeoutId = setTimeout(() => {
                setIsIdle(true);
            }, 3000);
        };
        document.addEventListener('mousemove', handleMouseMove);
        return () => {
            document.removeEventListener('mousemove', handleMouseMove);
            clearTimeout(timeoutId);
        };
    }, []);
    
    return (
        <div className={"bg-black w-screen h-screen"}>
            {renderControls && <VideoControls
                controls={false}
                className={`transition-opacity duration-500 ease-in-out ${isIdle ? 'opacity-0 cursor-none' : 'opacity-100'}`}/>
            }
        </div>
    )
}

export async function getServerSideProps(context: { res: any, params: { anime_id: string; episode_id: string; }; }) {
    const {anime_id, episode_id} = context.params
    const [anime, episode, streamingSources] = await AnimeService.getEpisodeLinks(anime_id, parseInt(episode_id));
    return {props: {anime: anime, episodeInfo: episode, streamingSources: streamingSources}}
}
