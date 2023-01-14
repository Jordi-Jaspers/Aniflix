import VideoPlayer from "@components/watch/VideoPlayer";
import {MediaSources} from "@interfaces/MediaSources";
import ConsumetApi from "@utils/ConsumetApi";
import React from "react";

interface Props {
    streamingSources: MediaSources;
}

export default function WatchAnime({streamingSources}: Props) {
    console.log(streamingSources);

    return (
        <>
            <VideoPlayer media={streamingSources}/>
        </>
    )
}

export async function getServerSideProps(context: { params: { anime_id: string; episode_id: string; }; }) {
    const {anime_id, episode_id} = context.params
    const streamingSources: MediaSources = await fetch(ConsumetApi.fetchEpisodeLinks.replace("{episodeId}", episode_id))
        .then((res) => res.json());

    return {
        props: {
            streamingSources: streamingSources
        }
    }
}
