import {animeState, episodeState, streamingLinksState,} from "@atoms/VideoPlayerAtom";
import VideoControls from "@components/watch/controls/VideoControls";
import {useFetchEpisode} from "@hooks/useFetchEpisode";
import {LOGGER} from "@util/Logger";

import React, {useEffect, useState} from "react";

import {useSetRecoilState} from "recoil";

interface Props {
    anime_id: string;
    episode_id: string;
}

export default function WatchAnime({anime_id, episode_id}: Props) {
    const {anime, episode, streamingSources, loading} = useFetchEpisode(anime_id, episode_id);
    const setEpisodeState = useSetRecoilState(episodeState);
    const setStreamingLinksState = useSetRecoilState(streamingLinksState);
    const setAnimeState = useSetRecoilState(animeState);
    const [isIdle, setIsIdle] = useState<boolean>(false);
    
    useEffect(() => {
        LOGGER.info("animeRes", anime)
        LOGGER.info("episodeRes", episode)
        LOGGER.info("sourcesRes", streamingSources)
        LOGGER.info("loading", loading)
        if (loading || !episode || !streamingSources || !anime) return;
        setEpisodeState(episode);
        setStreamingLinksState(streamingSources);
        setAnimeState(anime);
    }, [episode, streamingSources, anime, loading]);
    
    useEffect(() => {
        let timeoutId: NodeJS.Timeout;
        const handleMouseMove = () => {
            clearTimeout(timeoutId);
            setIsIdle(false);
            timeoutId = setTimeout(() => {
                setIsIdle(true);
            }, 3000);
        };
        document.addEventListener("mousemove", handleMouseMove);
        return () => {
            document.removeEventListener("mousemove", handleMouseMove);
            clearTimeout(timeoutId);
        };
    }, []);
    
    return (
        <div className={"h-screen w-screen bg-black"}>
            {!loading && <VideoControls
                    className={`transition-opacity duration-500 ease-in-out ${isIdle ? "cursor-none opacity-0" : "opacity-100"}`}/>
            }
        </div>
    );
}

export function getServerSideProps(context: { params: { anime_id: string; episode_id: string }; }) {
    const {anime_id, episode_id} = context.params;
    return {props: {anime_id: anime_id, episode_id: episode_id}};
}
