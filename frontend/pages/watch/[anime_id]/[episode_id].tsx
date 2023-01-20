import {animeState, episodeState, streamingLinksState} from "@atoms/VideoPlayerAtom";
import VideoControls from "@components/watch/VideoControls";
import {Anime} from "@interfaces/Anime";
import {Episode} from "@interfaces/Episode";
import {MediaSources} from "@interfaces/MediaSources";
import AnimeService from "@util/consumet/AnimeService";
import React, {useEffect} from "react";
import {useSetRecoilState} from "recoil";

interface Props {
    anime: Anime;
    episodeInfo: Episode;
    streamingSources: MediaSources;
}

export default function WatchAnime({anime, episodeInfo, streamingSources}: Props) {
    const [renderControls, setRenderControls] = React.useState<boolean>(false);
    const setEpisodeState = useSetRecoilState(episodeState);
    const setStreamingLinksState = useSetRecoilState(streamingLinksState);
    const setAnimeState = useSetRecoilState(animeState);
    
    useEffect(() => {
        setEpisodeState(episodeInfo);
        setStreamingLinksState(streamingSources);
        setAnimeState(anime);
        if (anime && episodeInfo && streamingSources) setRenderControls(true);
    }, [episodeInfo, streamingSources, anime]);
    
    return (
        <div className={"bg-black w-screen h-screen"}>
            {renderControls && <VideoControls controls={false}/>}
        </div>
    )
}

export async function getServerSideProps(context: { res: any, params: { anime_id: string; episode_id: string; }; }) {
    const {anime_id, episode_id} = context.params
    const [anime, episode, streamingSources] = await AnimeService.getEpisodeLinks(anime_id, parseInt(episode_id));
    console.info('the following objects are returned from the server: \n');
    console.info('anime: \n', anime);
    console.info('episodeInfo: \n', episode);
    console.info('streamingSources: \n', streamingSources);
    return {props: {anime: anime, episodeInfo: episode, streamingSources: streamingSources}}
}
