import RecommendationCard from "@components/RecommendationCard";
import {Recommendation} from "@interfaces/Recommendation";
import React from "react";

interface Props {
    className?: string;
    recommendations: Recommendation[]
    showMore?: boolean
}

export default function RecommendationCards({className, recommendations, showMore}: Props) {
    return (
        <div className={className ? className : ""}>
            <h4 className={"infoScreenTitle py-4"}>Recommendations</h4>
            <div className={"grid grid-cols-3 gap-[1em] justify-stretch align-stretch"}>
                {recommendations?.map((recommendation, index) => {
                    if (index < 9 || showMore ) return (<RecommendationCard key={recommendation.id} recommendation={recommendation}/>)
                })}
            </div>
        </div>
    )
}


