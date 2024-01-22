import React from "react";

import {HandThumbUpIcon} from "@heroicons/react/24/outline";

interface Props {
    buttonClassName?: string;
    iconClassName?: string;
}

export default function LikeButtonIcon({buttonClassName, iconClassName}: Props) {
    return (
        <button className={`${buttonClassName ? buttonClassName : ""} defaultButtonRound`}>
            <HandThumbUpIcon className={`${iconClassName ? iconClassName : ""} text-white`}/>
        </button>
    )
}