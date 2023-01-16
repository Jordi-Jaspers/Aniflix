import VideoPlayer from "@components/watch/VideoPlayer";
import {Episode} from "@interfaces/Episode";
import {MediaSources} from "@interfaces/MediaSources";
import {getEpisodeInformation, getEpisodeLinks} from "@utils/AnimeService";
import React from "react";

interface Props {
    streamingSources: MediaSources;
    episode: Episode;
}

export default function WatchAnime({streamingSources, episode}: Props) {
    console.log(streamingSources);

    return (
        <div className={"bg-black w-screen h-screen"}>
            <VideoPlayer controls={true} media={streamingSources} episode={episode}/>
        </div>
    )
}

export async function getServerSideProps(context: { params: { anime_id: string; episode_id: string; }; }) {
    const {anime_id, episode_id} = context.params
    const [streamingSources, episode] = await Promise.all([
        getEpisodeLinks(episode_id),
        getEpisodeInformation(anime_id, episode_id)
    ]);
    console.log(episode);
    return {props: {streamingSources: streamingSources, episode: episode}}
}
