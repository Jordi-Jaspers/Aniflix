

import {infoScreenState} from "@atoms/InfoScreenAtom";
import {Status} from "@enum/Status";
import AddToLibraryIcon from "@icons/AddToLibraryIcon";
import HoveringPlayIcon from "@icons/HoveringPlayIcon";
import LikeButtonIcon from "@icons/LikeButtonIcon";
import {Recommendation} from "@interfaces/Recommendation";

import React from "react";

import Image from "next/image";
import router from "next/router";
import {useSetRecoilState} from "recoil";



interface Props {
    recommendation: Recommendation
}

export default function RecommendationCard({recommendation}: Props) {
    const title: string = recommendation.title.romaji ? recommendation.title.romaji : recommendation.title.english;
    
    const setShowInfoScreen = useSetRecoilState(infoScreenState)
    const handleClickedRecommendation = async () => {
        setShowInfoScreen(false);
        await router.push('/watch/[anime_id]/[episode_id]', `/watch/${recommendation?.id}/1`)
    }
    
    // #1a1920
// #1E1E25
    return (
        <div
            className={"m-[0.1em] min-h-[18em] h-[100%] cursor-pointer rounded bg-[#1E1E25] shadow-lg border border-[#1E1E25] overflow-hidden"}>
            <div className={"relative overflow-hidden"} onClick={() => handleClickedRecommendation()}>
                <Image
                    src={recommendation.image}
                    alt={title}
                    className={"aspect-video object-cover brightness-75 z-10"}
                    width={2500}
                    height={2500}
                />
                <HoveringPlayIcon/>
                <span
                    className={"text-white font-poppins font-light absolute top-[5%] right-[5%]"}>{recommendation.episodes} Episodes</span>
            </div>
            <div className={"min-h-[100%] px-[1em]"}>
                <div className={"flex flex-row justify-between py-[1em]"}>
                    <div>
                        <p className="text-[#46d369] font-poppins">
                            {recommendation?.rating ? recommendation.rating : "0"}% match
                        </p>
                        <p className="text-[#666666] font-light font-poppins">
                            {recommendation?.status ? recommendation.status : Status.UNKNOWN}
                        </p>
                    </div>
                    <AddToLibraryIcon anime_id={recommendation.id} buttonClassName={"h-10 w-10"} iconClassName={"h-6 w-6"}/>
                </div>
                <p className={"text-[#d2d2d2] font-poppins font-light text-sm"}>
                    {title}
                </p>
            </div>
        </div>
    )
}


