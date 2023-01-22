import {infoScreenState} from "@atoms/InfoScreenAtom";
import {animeState} from "@atoms/VideoPlayerAtom";
import AddToLibraryIcon from "@icons/AddToLibraryIcon";
import HoveringPlayIcon from "@icons/HoveringPlayIcon";
import {Recommendation} from "@interfaces/Recommendation";
import Image from "next/image";
import router from "next/router";
import React from "react";
import {useRecoilValue, useSetRecoilState} from "recoil";

interface Props {
    anime: Recommendation
}

export default function RecommendationCard({anime}: Props) {
    const title: string = anime.title.romaji ? anime.title.romaji : anime.title.english;
    
    const setShowInfoScreen = useSetRecoilState(infoScreenState)
    const handleClickedRecommendation = async () => {
        setShowInfoScreen(false);
        await router.push('/watch/[anime_id]/[episode_id]', `/watch/${anime?.id}/1`)
    }
    
    return (
        <div className={"m-[0.1em] min-h-[18em] h-[100%] cursor-pointer rounded bg-[#2f2f2f] overflow-hidden"}>
            <div className={"relative overflow-hidden"} onClick={() => handleClickedRecommendation()}>
                <Image
                    src={anime.image}
                    alt={title}
                    className={"aspect-video object-cover brightness-75 z-10"}
                    width={2500}
                    height={2500}
                />
                <HoveringPlayIcon/>
                <span className={"text-white font-poppins font-light absolute top-[5%] right-[5%]"}>{anime.episodes} Episodes</span>
            </div>
            <div className={"min-h-[100%] px-[1em]"}>
                <div className={"flex flex-row justify-between py-[1em]"}>
                    <div>
                        <p className="text-[#46d369]  font-poppins">
                            {anime?.rating ? anime.rating : "0"}% match
                        </p>
                        <p className="text-white font-light font-poppins">
                            {anime?.status ? anime.status : "Unknown"}
                        </p>
                    </div>
                    <AddToLibraryIcon/>
                </div>
                <p className={"text-[#d2d2d2] font-poppins font-light text-sm"}>
                    {title}
                </p>
            </div>
        </div>
    )
}


