import {
    currentTimeState,
    durationState,
    episodeState,
    isMuteState,
    isPlayingState,
    videoPlayerState,
    volumeState
} from "@atoms/VideoPlayerAtom";
import EpisodesMenu from "@components/watch/controls/EpisodesMenu";
import ResolutionMenu from "@components/watch/controls/ResolutionMenu";
import VolumeControls from "@components/watch/controls/VolumeControls";
import VideoPlayer from "@components/watch/VideoPlayer";
import {PRIVATE_PATHS} from "@enum/Paths";
import {ArrowsPointingOutIcon} from "@heroicons/react/24/outline";
import {ArrowLeftIcon, PauseIcon, PlayIcon} from "@heroicons/react/24/solid";
import Forward from "@svg/Forward.svg";
import Forward120 from "@svg/Forward120.svg";
import NextEpisode from "@svg/NextEpisode.svg";
import Rewind from "@svg/Rewind.svg";
import {LOGGER} from "@util/Logger";
import Image from "next/image";
import {useRouter} from "next/router";
import React, {createRef, useEffect, useRef, useState} from 'react';
import Slider from "react-input-slider";
import {useRecoilValue, useSetRecoilState} from "recoil";

interface Props {
    className?: string;
}

export default function VideoControls({className}: Props) {
    const router = useRouter();
    const videoRef = createRef<HTMLVideoElement>();
    const buttonRef = useRef<HTMLButtonElement>(null);
    
    const setCurrentTime = useSetRecoilState(currentTimeState);
    const setIsPlaying = useSetRecoilState(isPlayingState);
    const setDuration = useSetRecoilState(durationState);
    const setEpisode = useSetRecoilState(episodeState);
    const setVolume = useSetRecoilState(volumeState);
    const setMuted = useSetRecoilState(isMuteState);
    const [isHoveringVolume, setIsHoveringVolume] = useState<boolean>(false);
    const {anime, episode, streamingLinks, volume, isMuted, isPlaying, duration, currentTime} = useRecoilValue(videoPlayerState);
    
    const remainingTime = duration - currentTime;
    const minutes: string = Math.floor(remainingTime / 60).toString().padStart(2, '0');
    const seconds: string = Math.floor(remainingTime % 60).toString().padStart(2, '0');
    
    const handleTimeChange = (time: number) => {
        const videoPlayer = videoRef.current;
        if (!videoPlayer) return;
        videoPlayer.currentTime = time
        setCurrentTime(time);
    }
    
    const handleFocus = () => {
        if (!buttonRef.current) return;
        buttonRef.current.focus();
    }
    
    const handleKeyDown = (e: React.KeyboardEvent<HTMLDivElement>) => {
        console.debug(e.key)
        if (!videoRef.current) return;
        if (e.key === ' ') {
            e.preventDefault();
            setIsPlaying(!isPlaying);
        }
        if (e.key === 'ArrowLeft') {
            e.preventDefault();
            handlePlayback(-10);
        }
        if (e.key === 'ArrowRight') {
            e.preventDefault();
            handlePlayback(10);
        }
        if (e.key === 'ArrowUp') {
            e.preventDefault();
            setVolume(volume + 5);
        }
        if (e.key === 'ArrowDown') {
            e.preventDefault();
            setVolume(volume - 5);
        }
        if (e.key === 'm') {
            e.preventDefault();
            setMuted(!isMuted);
        }
        if (e.key === 'f') {
            e.preventDefault();
            toggleFullScreen();
        }
    }
    
    const nextEpisode = () => {
        if (anime.episodes.length > episode.number) {
            LOGGER.debug(`[VideoControls] Going to episode ${episode.number + 1} of ${anime.episodes.length}`);
            setEpisode(anime.episodes[episode.number]);
            router.push(`/watch/${anime.id}/${episode.number + 1}`).catch((error) => {
                LOGGER.error('[VideoControls] Failed to push to next episode', error);
            });
        }
    }
    
    const toggleFullScreen = () => {
        const videoPlayer = videoRef.current;
        if (!videoPlayer) return;
        videoPlayer.requestFullscreen({navigationUI: "hide"}).catch((error) => {
            LOGGER.error('[VideoControls] Error while trying to fullscreen video', error);
        });
    }
    
    const handlePlayback = (seconds: number) => {
        const videoPlayer = videoRef.current;
        if (!videoPlayer) return;
        videoPlayer.currentTime += seconds;
        setCurrentTime(videoPlayer.currentTime);
    }
    
    useEffect(() => {
        const videoPlayer = videoRef.current;
        if (!videoPlayer) return;
        setCurrentTime(videoPlayer.currentTime);
        setDuration(videoPlayer.duration);
        
        if (volume > 100) {
            setVolume(100);
            return;
        }
        if (volume < 0) {
            setVolume(0);
            return;
        }
        
        videoPlayer.volume = volume / 100;
        videoPlayer.muted = isMuted;
        videoPlayer.ontimeupdate = () => setCurrentTime(videoPlayer.currentTime);
        
        if (isPlaying) {
            videoPlayer.play().then(() => {
                setIsPlaying(true);
            }).catch((error) => {
                LOGGER.error('[VideoControls] Error while trying to play video', error);
                setIsPlaying(false);
            });
        } else {
            videoPlayer.pause();
            setIsPlaying(false);
        }
    }, [videoRef, volume, isMuted, isPlaying]);
    
    return (
        <>
            <VideoPlayer innerRef={videoRef}/>
            <div className={`${className ? className : ''} fixed top-0 bottom-0 h-screen w-screen bg-black/20 pt-4 z-10`}
                 onClick={() => handleFocus()} onKeyDown={(e) => handleKeyDown(e)}>
                {/* Top part of video controls */}
                <div className={"absolute w-screen top-0 h-16"}>
                    <div className="flex h-full items-center px-4 py-2">
                        {/* go back to home page after click on button */}
                        <button className="videoPlayerControls" onClick={() => router.push(PRIVATE_PATHS.HOME)}>
                            <ArrowLeftIcon className={"h-full w-full"}/>
                        </button>
                    </div>
                </div>
                
                {/* Bottom part of the video controls */}
                <div className="absolute w-screen bottom-0 pb-4 px-4 h-auto">
                    <div
                        className={`${isHoveringVolume ? "hidden" : "block"} w-full h-full flex flex-row justify-center items-center space-x-4`}>
                        <Slider
                            axis="x"
                            x={currentTime}
                            xmin={0}
                            xmax={duration}
                            onChange={({x}) => handleTimeChange(x)}
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
                    <div className="flex justify-between items-end py-2 h-full">
                        <div className="flex items-end justify-evenly space-x-4">
                            <button className="videoPlayerControls" onClick={() => setIsPlaying(!isPlaying)} ref={buttonRef}>
                                {isPlaying ? <PauseIcon className={"h-full w-full"}/> : <PlayIcon className={"h-full w-full"}/>}
                            </button>
                            <button className="videoPlayerControls" onClick={() => handlePlayback(-30)}>
                                <Image width={64} height={64} src={Rewind} alt={"Rewind"}/>
                            </button>
                            <button className="videoPlayerControls" onClick={() => handlePlayback(+30)}>
                                <Image width={64} height={64} src={Forward} alt={"Forward"}/>
                            </button>
                            <button className="videoPlayerControls" onClick={() => handlePlayback(+120)}>
                                <Image width={64} height={64} src={Forward120} alt={"Forward 120s"}/>
                            </button>
                        </div>
                        <div className="hidden sm:block text-white text-sm text-center font-poppins items-end">
                            <p className="font-bold text-lg">{(anime.title.romaji ? anime.title.romaji : anime.title.english) as string}</p>
                            <p className="text-gray-400 font-light"> Episode {episode.number}{episode.title ? (": " + episode.title) : ""}</p>
                        </div>
                        <div className="flex items-end justify-evenly space-x-2 sm:space-x-4">
                            <button className="videoPlayerControls" onClick={() => nextEpisode()}>
                                <Image width={64} height={64} src={NextEpisode} alt={"Next Episode"} className={"h-full w-full"}/>
                            </button>
                            <div className="w-6 sm:w-10 flex flex-col items-center items-end accent-gray-700"
                                 onMouseEnter={() => setIsHoveringVolume(true)}
                                 onMouseLeave={() => setIsHoveringVolume(false)}>
                                <VolumeControls className={`${isHoveringVolume ? "block" : "hidden"} mb-2`}/>
                            </div>
                            <EpisodesMenu episodes={anime.episodes}/>
                            <ResolutionMenu mediaSources={streamingLinks.sources}/>
                            <button className="videoPlayerControls" onClick={() => toggleFullScreen()}>
                                <ArrowsPointingOutIcon className={"h-full w-full"}/>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}
