import {infoScreenState} from "@atoms/InfoScreenAtom";
import {animeState} from "@atoms/VideoPlayerAtom";
import HoveringPlayIcon from "@icons/HoveringPlayIcon";
import {Episode} from "@interfaces/Episode";
import Image from "next/image";
import router from "next/router";
import React from "react";
import {useRecoilValue, useSetRecoilState} from "recoil";

interface Props {
    episode: Episode
}

export default function EpisodeCard({episode}: Props) {
    let date = new Date(episode.airDate);
    let airDate = date.toLocaleDateString();
    
    const setShowInfoScreen = useSetRecoilState(infoScreenState)
    const anime = useRecoilValue(animeState);
    
    const handleClickedEpisode = async () => {
        setShowInfoScreen(false);
        await router.push('/watch/[anime_id]/[episode_id]', `/watch/${anime?.id}/${episode.number}`)
    }
    
    return (
        <div className={"flex justify-center"} onClick={() => handleClickedEpisode()}>
            <div className={"border-y-[1px] border-[#404040] min-h-[6em] rounded w-full flex px-6 py-8 cursor-pointer hover:bg-[#1E1E25]"}>
                <div className={"text-[#d2d2d2] text-2xl min-w-fit h-fit font-poppins text-center self-center pr-6"}>
                    {episode?.number}
                </div>
                <div className={"relative flex justify-center items-center overflow-hidden max-w-[20%] aspect-video rounded-xl "}>
                    <Image
                        src={episode.image}
                        alt={episode.title}
                        className={"object-cover rounded-xl"}
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
