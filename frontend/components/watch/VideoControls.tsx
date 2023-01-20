import {currentTimeState, durationState, resolutionState, videoPlayerState, volumeState} from "@atoms/VideoPlayerAtom";
import VideoPlayer from "@components/watch/VideoPlayer";
import {AdjustmentsHorizontalIcon, ArrowsPointingOutIcon, ForwardIcon} from "@heroicons/react/24/outline";
import {ArrowLeftIcon, PauseIcon, PlayIcon} from "@heroicons/react/24/solid";
import ResolutionMenu from "@icons/ResolutionMenu";
import ChromeCastIcon from "@svg/ChromeCastIcon.svg";
import Forward from "@svg/Forward.svg";
import PlaybackSpeedIcon from "@svg/PlaybackSpeedIcon.png";
import Rewind from "@svg/Rewind.svg";
import SpeakerHigh from "@svg/SpeakerHigh.svg";
import SpeakerLow from "@svg/SpeakerLow.svg";
import SpeakerOff from "@svg/SpeakerOff.svg";
import Image from "next/image";
import {useRouter} from "next/router";
import React, {useEffect, useState} from 'react';
import Slider from 'react-input-slider';
import {useRecoilValue, useSetRecoilState} from "recoil";

interface Props {
    className?: string;
    controls: boolean;
}

export default function VideoControls({className, controls}: Props) {
    const router = useRouter();
    const videoRef = React.createRef<HTMLVideoElement>();
    
    const setVolume = useSetRecoilState(volumeState);
    const setCurrentTime = useSetRecoilState(currentTimeState);
    const setDuration = useSetRecoilState(durationState);
    const setResolution = useSetRecoilState(resolutionState);
    const {anime, episode, streamingLinks, volume, currentTime, duration, resolution} = useRecoilValue(videoPlayerState);
    
    const [isMuted, setIsMuted] = useState<boolean>(false);
    const [isPlaying, setIsPlaying] = useState<boolean>(true);
    const [isHoveringVolume, setIsHoveringVolume] = useState<boolean>(false);
    
    useEffect(() => {
        if (!videoRef.current) return;
        setCurrentTime(videoRef.current.currentTime);
        setDuration(videoRef.current.duration);
        
        if (videoRef.current.src !== resolution) {
            videoRef.current.src = resolution;
            videoRef.current.load();
        }
        
        if (volume) {
            if (volume <= 0) mute(true);
            else videoRef.current.volume = volume / 100;
        }
    }, [volume, duration, currentTime, videoRef, resolution]);
    
    function toggleResolution(value: string) {
        if (!videoRef.current) return;
        for (const link of streamingLinks.sources) {
            if (link.quality === value) {
                setResolution(value);
                videoRef.current.src = link.url;
                videoRef.current.load();
                break;
            }
        }
    }
    
    function togglePlay() {
        if (!videoRef.current) return;
        if (!isPlaying) {
            const playPromise = videoRef.current.play();
            if (playPromise !== undefined) {
                playPromise.then(() => {
                    setIsPlaying(true);
                }).catch((error) => {
                    console.error('Error while trying to play video', error);
                });
            }
        } else {
            videoRef.current.pause();
            setCurrentTime(videoRef.current.currentTime);
            setIsPlaying(false);
        }
    }
    
    function forwardRewind(seconds: number) {
        if (!videoRef.current) return;
        videoRef.current.currentTime += seconds;
        setCurrentTime(videoRef.current.currentTime);
    }
    
    function mute(value: boolean) {
        if (!videoRef.current) return;
        videoRef.current.muted = false;
        setIsMuted(false);
    }
    
    return (
        <div>
            <VideoPlayer media={streamingLinks} controls={controls} innerRef={videoRef}/>
            <div className={`${className ? className : ''} fixed top-0 bottom-0 h-screen w-screen bg-black/20 pt-4 z-10`}>
                {/* Top part of video controls */}
                <div className={"absolute w-screen top-0 h-16"}>
                    <div className="flex h-full items-center px-4 py-2">
                        {/* go back to home page after click on button */}
                        <button className="flex items-center text-white h-10 w-10 cursor-pointer" onClick={() => router.back()}>
                            <ArrowLeftIcon className={"h-full w-full"}/>
                        </button>
                    </div>
                </div>
                
                {/* Bottom part of the video controls */}
                <div className="absolute w-screen bottom-0 pb-4 px-4 h-auto">
                    <div className={`${isHoveringVolume ? "hidden" : "block"} w-full h-full flex flex-row justify-center items-center space-x-4`}>
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
                        {/* Show the remaining time left of the video in "00:00" format */}
                        <div className={"font-poppins text-white px-2"}>{Math.floor((duration - currentTime) / 60).toString().padStart(2, '0')}:{Math.floor((duration - currentTime) % 60).toString().padStart(2, '0')}</div>
                    </div>
                    <div className="flex justify-between items-end px-4 py-2 h-full">
                        <div className="flex items-end justify-evenly space-x-6">
                            <button className="videoPlayerControls" onClick={() => togglePlay()}>
                                {isPlaying ? <PauseIcon className={"h-full w-full"}/> : <PlayIcon className={"h-full w-full"}/>}
                            </button>
                            <button className="videoPlayerControls h-9 w-9" onClick={() => forwardRewind(-30)}>
                                <Image width={64} height={64} src={Rewind} alt={"Rewind"}/>
                            </button>
                            <button className="videoPlayerControls h-9 w-9" onClick={() => forwardRewind(+30)}>
                                <Image width={64} height={64} src={Forward} alt={"Forward"}/>
                            </button>
                            
                            <div className="w-10 flex flex-col items-center items-end accent-gray-700"
                                 onMouseEnter={() => setIsHoveringVolume(true)}
                                 onMouseLeave={() => setIsHoveringVolume(false)}>
                                <div className={`${isHoveringVolume ? "block" : "hidden"} mb-2`}>
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
                                <button className="videoPlayerControls" onClick={() => mute(!isMuted)}>
                                    {(volume == 0 || isMuted) && <Image width={64} height={64} src={SpeakerOff} alt={"Speaker Off"}/>}
                                    {(volume <= 50 && volume != 0 && !isMuted) &&
                                        <Image width={64} height={64} src={SpeakerLow} alt={"Speaker Low"}/>}
                                    {(volume > 50 && !isMuted) && <Image width={64} height={64} src={SpeakerHigh} alt={"Speaker High"}/>}
                                </button>
                            </div>
                        </div>
                        <div className="text-white text-sm text-center font-poppins items-end">
                            <p className="font-bold text-lg">{(anime.title.romaji ? anime.title.romaji : anime.title.english) as string}</p>
                            <p className="text-gray-400 font-light"> Episode {episode.number}{episode.title ? (": " + episode.title) : ""}</p>
                        </div>
                        <div className="flex items-center justify-evenly space-x-6">
                            <button className="videoPlayerControls">
                                <ForwardIcon className={"h-full w-full"}/>
                            </button>
                            <ResolutionMenu mediaSources={streamingLinks.sources}/>
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
        </div>
    )
}
