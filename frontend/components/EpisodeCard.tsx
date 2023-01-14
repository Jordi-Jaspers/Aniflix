import Image from "next/image";
import React from "react";
import {Episode} from "@interfaces/Episode";
import HoveringPlayIcon from "@icons/HoveringPlayIcon";

interface Props {
    episode: Episode
}

export default function EpisodeCard({episode}: Props) {
    let date = new Date(episode.airDate);
    let airDate = date.toLocaleDateString();

    return (
        <div className={"flex justify-center"}>
            <div className={"border-y-[1px] border-[#404040] min-h-[6em] rounded w-full flex px-6 py-8 cursor-pointer hover:bg-[#333]"}>
                <div className={"text-[#d2d2d2] text-2xl min-w-fit h-fit font-poppins text-center self-center pr-6"}>
                    {episode?.number}
                </div>
                <div className={"relative overflow-hidden max-w-[20%] aspect-video rounded-xl"}>
                    <Image
                        src={episode.image}
                        alt={episode.title}
                        className={"object-cover self-center"}
                        width={2000}
                        height={2000}
                    />
                    <HoveringPlayIcon/>
                </div>
                <div>
                    <div className={"pl-6"}>
                        <div className={"flex flex-row justify-between pb-2"}>
                            <div className={"text-white font-bold font-poppins"}>
                                {episode.title}
                            </div>
                            <div className={"text-white font-bold font-poppins"}>
                                {airDate}
                            </div>
                        </div>
                        <p className={"text-[#d2d2d2] font-poppins font-light text-sm"}>
                            {episode.description}
                        </p>
                    </div>
                    <p></p>
                </div>
            </div>
        </div>
    )
}
