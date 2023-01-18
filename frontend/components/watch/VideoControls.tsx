import VideoPlayer from "@components/watch/VideoPlayer";
import {AdjustmentsHorizontalIcon, ArrowsPointingOutIcon, ForwardIcon} from "@heroicons/react/24/outline";
import {ArrowLeftIcon, PauseIcon, PlayIcon} from "@heroicons/react/24/solid";
import {Episode} from "@interfaces/Episode";
import {MediaSources} from "@interfaces/MediaSources";
import ChromeCastIcon from "@svg/ChromeCastIcon.svg";
import Forward from "@svg/Forward.svg";
import PlaybackSpeedIcon from "@svg/PlaybackSpeedIcon.png";
import Rewind from "@svg/Rewind.svg";
import SpeakerHigh from "@svg/SpeakerHigh.svg";
import SpeakerLow from "@svg/SpeakerLow.svg";
import SpeakerOff from "@svg/SpeakerOff.svg";
import Image from "next/image";
import {useRouter} from "next/router";
import React, {useState} from 'react';

interface Props {
    className?: string;
    media: MediaSources;
    episode: Episode;
    controls: boolean;
}

export default function VideoControls({className, media, episode, controls}: Props) {
    const router = useRouter();
    const videoRef = React.createRef<HTMLVideoElement>();
    const title = (): string => {
        return episode.id.includes('episode')
            ? episode.id.split('-episode')[0].replaceAll("-", " ")
            : episode.id.replaceAll("-", " ")
    }
    
    const [durationOfVideo, setDurationOfVideo] = useState(0);
    const [currentDurationOfVideo, setCurrentDurationOfVideo] = useState(0);
    const getDurationOfVideo = () => {
        if (!videoRef.current) return;
        const currentTime: number = videoRef.current.currentTime;
        const videoIntervalTime = setInterval(() => {
            setCurrentDurationOfVideo(currentTime);
            if (currentTime >= durationOfVideo) clearVideoInterval()
        }, 1000)
        const clearVideoInterval = () => {
            clearInterval(videoIntervalTime);
        }
    }
    
    const [isPlaying, setIsPlaying] = useState<boolean>(true);
    
    function togglePlay() {
        if (!videoRef.current) return;
        if (isPlaying) {
            const playPromise = videoRef.current.play();
            if (playPromise !== undefined) {
                playPromise.then(() => {
                    console.debug('Auto play started');
                }).catch((error) => {
                    console.error('Auto play failed', error);
                });
            }
            setDurationOfVideo(videoRef.current.duration);
            getDurationOfVideo();
        } else {
            videoRef.current.pause();
            setIsPlaying(false);
        }
    }
    
    function forward() {
        if (!videoRef.current) return;
        videoRef.current.currentTime += 30;
        setCurrentDurationOfVideo(videoRef.current.currentTime);
    }
    
    function rewind() {
        if (!videoRef.current) return;
        videoRef.current.currentTime -= 30;
        setCurrentDurationOfVideo(videoRef.current.currentTime);
    }
    
    const [volume, setVolume] = useState<number>(100);
    const changeVolume = (e) => {
        if (!videoRef.current) return;
        const targetVolume = parseFloat(e.target.value) / 100;
        setVolume(targetVolume);
        videoRef.current.volume = Number(volume.toFixed(1));
    }
    
    const [isMuted, setIsMuted] = useState<boolean>(false);
    
    function mute() {
        if (!videoRef.current) return;
        if (isMuted) {
            videoRef.current.muted = false;
            setIsMuted(false);
        } else {
            videoRef.current.muted = true;
            setIsMuted(true);
        }
    }
    
    return (
        <>
            <VideoPlayer media={media} controls={controls} innerRef={videoRef}/>
            <div className={`${className ? className : ''} fixed top-0 bottom-0 h-screen w-screen bg-black/20 z-10`}>
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
                <div className="absolute w-screen bottom-0 h-auto">
                    <div className="flex justify-between items-center px-4 py-2 h-full">
                        <div className="flex items-center justify-evenly space-x-6">
                            <button className="videoPlayerControls" onClick={() => togglePlay()}>
                                {isPlaying ? <PauseIcon className={"h-full w-full"}/> : <PlayIcon className={"h-full w-full"}/>}
                            </button>
                            <button className="videoPlayerControls" onClick={() => rewind()}>
                                <Image width={64} height={64} src={Rewind} alt={"Rewind"}/>
                            </button>
                            <button className="videoPlayerControls" onClick={() => forward()}>
                                <Image width={64} height={64} src={Forward} alt={"Forward"}/>
                            </button>
                            
                            <div className="w-10 fit flex flex-col items-center justify-center group">
                                <input
                                    type="range"
                                    min={0}
                                    max={100}
                                    value={volume}
                                    onChange={changeVolume}
                                    className="invisible group-hover:visible w-24 h-2 -rotate-90 rounded-full -translate-y-14 -translate-x-1 bg-gray-800 cursor-pointer appearance-none"
                                    id="volume-slider"
                                />
                                <button className="videoPlayerControls h-12 w-12 mb-2" onClick={() => mute()}>
                                    {(volume == 0 || isMuted) && <Image width={64} height={64} src={SpeakerOff} alt={"Speaker Off"}/>}
                                    {(volume <= 50 && volume != 0 && !isMuted) &&
                                        <Image width={64} height={64} src={SpeakerLow} alt={"Speaker Low"}/>}
                                    {(volume > 50 && !isMuted) && <Image width={64} height={64} src={SpeakerHigh} alt={"Speaker High"}/>}
                                </button>
                            </div>
                        </div>
                        <div className="text-white text-sm text-center font-poppins">
                            <p className="font-bold text-lg">{title()}</p>
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
        </>
    )
}
