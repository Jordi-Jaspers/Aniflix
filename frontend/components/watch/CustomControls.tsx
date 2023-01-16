import {AdjustmentsHorizontalIcon, ArrowsPointingOutIcon, ForwardIcon} from "@heroicons/react/24/outline";
import {ArrowLeftIcon, PlayIcon} from "@heroicons/react/24/solid";
import {Anime} from "@interfaces/Anime";
import {Episode} from "@interfaces/Episode";
import ChromeCastIcon from "@svg/ChromeCastIcon.svg";
import Forward from "@svg/Forward.svg";
import PlaybackSpeedIcon from "@svg/PlaybackSpeedIcon.png";
import Rewind from "@svg/Rewind.svg";
import SpeakerHigh from "@svg/SpeakerHigh.svg";
import SpeakerLow from "@svg/SpeakerLow.svg";
import SpeakerOff from "@svg/SpeakerOff.svg";
import Image from "next/image";
import React from 'react';

interface Props {
    className?: string;
    ref: React.RefObject<HTMLVideoElement>;
    episode: Episode;
}

export default function CustomControls({className, ref, episode}: Props) {
    const [volume, setVolume] = React.useState(100);
    const [isMuted, setIsMuted] = React.useState(false);

    return (
        <div className={`${className ? className : ''} fixed top-0 bottom-0 h-screen w-screen bg-black/80 z-10`}>
            {/* Top part of video controls */}
            <div className={"absolute w-screen top-0 h-16"}>
                <div className="flex h-full items-center px-4 py-2">
                    <button className="flex items-center text-white h-10 w-10 cursor-pointer">
                        <ArrowLeftIcon className={"h-full w-full"}/>
                    </button>
                </div>
            </div>
            {/* Bottom part of the video controls */}
            <div className="absolute w-screen bottom-0 h-16">
                <div className="flex justify-between items-center px-4 py-2">
                    <div className="flex items-center justify-evenly space-x-6">
                        <button className="videoPlayerControls">
                            <PlayIcon className={"h-full w-full"}/>
                        </button>
                        <button className="videoPlayerControls">
                            <Image width={64} height={64} src={Rewind} alt={"Rewind"}/>
                        </button>
                        <button className="videoPlayerControls">
                            <Image width={64} height={64} src={Forward} alt={"Forward"}/>
                        </button>
                        <button className="videoPlayerControls">
                            {isMuted && <Image width={64} height={64} src={SpeakerOff} alt={"Speaker Off"}/>}
                            {!isMuted && volume <= 50 && <Image width={64} height={64} src={SpeakerLow} alt={"Speaker Low"}/>}
                            {!isMuted && volume > 50 && <Image width={64} height={64} src={SpeakerHigh} alt={"Speaker High"}/>}
                        </button>
                    </div>
                    <div className="text-white text-sm text-center font-poppins">
                        <p className="font-bold ">{episode.id.split('-episode')[0]}</p>
                        <p className="text-gray-400 font-light"> Episode {episode.number}: {episode.title}</p>
                    </div>
                    <div className="flex items-center justify-evenly space-x-6">
                        <button className="videoPlayerControls">
                            <ForwardIcon className={"h-full w-full"}/>
                        </button>
                        <button className="videoPlayerControls">
                            <AdjustmentsHorizontalIcon className={"h-full w-full"}/>
                        </button>
                        <button className="videoPlayerControls">
                            <Image width={64} height={64} src={PlaybackSpeedIcon} alt={"Playback Speed"}/>
                        </button>
                        <button className="videoPlayerControls">
                            <Image width={64} height={64} src={ChromeCastIcon} alt={"Chromecast"}/>
                        </button>
                        <button className="videoPlayerControls">
                            <ArrowsPointingOutIcon className={"h-full w-full"}/>
                        </button>
                    </div>
                </div>
            </div>
        </div>

    )
}
