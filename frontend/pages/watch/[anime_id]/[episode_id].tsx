import VideoControls from "@components/watch/VideoControls";
import VideoPlayer from "@components/watch/VideoPlayer";
import {Episode} from "@interfaces/Episode";
import {MediaSources} from "@interfaces/MediaSources";
import AnimeService from "@util/consumet/AnimeService";
import React from "react";

interface Props {
    streamingSources: MediaSources;
    episode: Episode;
}

export default function WatchAnime({streamingSources, episode}: Props) {
    console.log(streamingSources);

    return (
        <div className={"bg-black w-screen h-screen"}>
            <VideoControls media={streamingSources} episode={episode} controls={false}/>
        </div>
    )
}

export async function getServerSideProps(context: { params: { anime_id: string; episode_id: string; }; }) {
    const {anime_id, episode_id} = context.params
    const [episode, streamingSources] = await AnimeService.getEpisodeLinks(anime_id, parseInt(episode_id));
    return {props: {streamingSources: streamingSources, episode: episode}}
}
