import {currentTimeState, durationState} from "@atoms/VideoPlayerAtom";
import React from 'react';
import Slider from "react-input-slider";
import {useRecoilState, useRecoilValue} from "recoil";

interface Props {
    className?: string;
    duration: number;
}

export default function TimelineControls({className, duration}: Props) {
    const [currentTime, setCurrentTime] = useRecoilState(currentTimeState);
    const remainingTime = duration - currentTime;
    const minutes: string = Math.floor(remainingTime / 60).toString().padStart(2, '0');
    const seconds: string = Math.floor(remainingTime % 60).toString().padStart(2, '0');

    return (
        <div className={`${className ? className : ""} w-full h-full flex flex-row justify-center items-center space-x-4`}>
            <Slider
                axis="x"
                x={currentTime}
                xmin={0}
                xmax={duration}
                onChange={({x}) => setCurrentTime(x)}
                styles={{
                    track: {
                        width: '100%',
                        height: 3,
                        backgroundColor: '#374151',
                        borderRadius: 2,
                        cursor: 'pointer',
                    },
                    active: {
                        backgroundColor: '#f00',
                    },
                    thumb: {
                        width: 10,
                        height: 10,
                        backgroundColor: '#f00',
                        borderRadius: 10,
                        cursor: 'pointer',
                    }
                }}
            />
            <div className={"font-poppins text-white px-2"}>{minutes}:{seconds}</div>
        </div>
    )
}
