import RecommendationCard from "@components/RecommendationCard";
import {Recommendation} from "@interfaces/Recommendation";
import React from "react";

interface Props {
    className?: string;
    recommendations: Recommendation[]
}

export default function RecommendationCards({className, recommendations}: Props) {
    return (
        <div className={className ? className : ""}>
            <h4 className={"infoScreenTitle py-4"}>Recommendations</h4>
            <div className={"grid grid-cols-3 gap-[1em] justify-stretch align-stretch"}>
                {recommendations?.map((recommendation) => (
                    <RecommendationCard anime={recommendation}/>
                ))}
            </div>
        </div>
    )
}


