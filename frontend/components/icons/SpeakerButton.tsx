import React from "react";

import {SpeakerWaveIcon, SpeakerXMarkIcon} from "@heroicons/react/24/outline";

interface Props {
    buttonClassName?: string;
    iconClassName?: string;
    isPlaying: boolean;
    isMuted: boolean;
    onClick: () => void;
}

export default function SpeakerButton({buttonClassName, iconClassName, isMuted, isPlaying, onClick}: Props) {
    return (
        <button
            className={`${isPlaying ? `${buttonClassName ? buttonClassName : ""} defaultButtonRound` : "hidden"}`}
            onClick={onClick}>
            {isMuted
                ? <SpeakerXMarkIcon className={`${iconClassName ? iconClassName : ""} text-white`}/>
                : <SpeakerWaveIcon className={`${iconClassName ? iconClassName : ""} text-white`}/>
            }
        </button>
    )
};
