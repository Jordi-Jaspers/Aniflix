import EpisodeCard from "@components/EpisodeCard";
import {Episode} from "@interfaces/Episode";
import React from "react";

interface Props {
    className?: string;
    episodes: Episode[]
}

export default function EpisodeCards({className, episodes}: Props) {
    return (
        <div className={className ? className : ""}>
            <div className="flex flex-row justify-between rounded-b-md py-4">
                <h4 className={"infoScreenTitle py-0"}>Episodes</h4>
                <p className={"text-lg text-white w-fit py-1.5 px-8 bg-[#242424] border-[#4d4d4d] border rounded text-center"}>Total: {episodes?.length}</p>
            </div>
            {episodes?.map((episode) => (<EpisodeCard key={episode.number} episode={episode}/>))}
        </div>
    )
}
