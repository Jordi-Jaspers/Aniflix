import {isMuteState, volumeState} from "@atoms/VideoPlayerAtom";
import SpeakerHigh from "@svg/SpeakerHigh.svg";
import SpeakerLow from "@svg/SpeakerLow.svg";
import SpeakerOff from "@svg/SpeakerOff.svg";
import Image from "next/image";
import React, {useEffect} from 'react';
import Slider from "react-input-slider";
import {useRecoilState} from "recoil";

interface Props {
    className?: string;
}

export default function VolumeControls({className}: Props) {
    const [volume, setVolume] = useRecoilState(volumeState);
    const [isMuted, setIsMuted] = useRecoilState(isMuteState);
    
    useEffect(() => {
        if (volume === 0) setIsMuted(true);
    }, [volume, isMuted]);
    
    return (
        <>
            <div className={`${className ? className : ""}`}>
                <Slider
                    axis="y"
                    yreverse={true}
                    y={isMuted ? 0 : volume}
                    ymin={0}
                    ymax={100}
                    onChange={({y}) => setVolume(y ? y : volume)}
                    styles={{
                        track: {
                            width: 8,
                            height: 100,
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
                        },
                    }}
                />
            </div>
            <button className="videoPlayerControls" onClick={() => setIsMuted(!isMuted)}>
                {(volume === 0 || isMuted) && <Image width={64} height={64} src={SpeakerOff} alt={"Speaker Off"}/>}
                {(volume <= 50 && volume != 0 && !isMuted) && <Image width={64} height={64} src={SpeakerLow} alt={"Speaker Low"}/>}
                {(volume > 50 && !isMuted) && <Image width={64} height={64} src={SpeakerHigh} alt={"Speaker High"}/>}
            </button>
        </>
    )
}
