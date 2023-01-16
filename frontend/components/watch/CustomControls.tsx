import Hls from "hls.js";
import React, {useState} from 'react';

interface Props {
    ref: React.RefObject<HTMLVideoElement>;
}

export default function CustomControls({ref}: Props) {
    const [isPlaying, setIsPlaying] = useState(false);
    const [currentTime, setCurrentTime] = useState(0);
    const [duration, setDuration] = useState(0);
    const [isMuted, setIsMuted] = useState(false);
    const [volume, setVolume] = useState(0);
    const [isFullscreen, setIsFullscreen] = useState(false);
    const [quality, setQuality] = useState(0);

    const video = ref.current;

    const handlePlayPause = () => {
        if (isPlaying) {
            video.pause();
        } else {
            video.play();
        }
        setIsPlaying(!isPlaying);
    }

    const handleMute = () => {
        if (isMuted) {
            video.muted = false;
        } else {
            video.muted = true;
        }
        setIsMuted(!isMuted);
    }

    const handleFullscreen = () => {
        if (isFullscreen) {
            document.exitFullscreen();
        } else {
            video.requestFullscreen();
        }
        setIsFullscreen(!isFullscreen);
    }

    const handleVolumeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        video.volume = parseFloat(e.target.value);
        setVolume(parseFloat(e.target.value));

    }

    const handleTimeUpdate = (e: React.ChangeEvent<HTMLInputElement>) => {
        video.currentTime = parseFloat(e.target.value);
        setCurrentTime(parseFloat(e.target.value));
    }

    const handleQualityChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setQuality(parseInt(e.target.value));
    }

    const handleLoadedMetadata = () => {
        setDuration(video.duration);
        setVolume(video.volume);

    }

    return (
        <div className="absolute bottom-0 left-0 w-full h-20 bg-black bg-opacity-50">
            <div className="flex flex-row justify-between items-center h-full px-4">
                <div className="flex flex-row items-center">
                    <button onClick={handlePlayPause}>
                        {isPlaying ? (
                            <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 12M6 6l12 6" />
                            </svg>
                        ) : (
                            <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 15l7-7 7 7M5 9l7 7 7-7" />
                            </svg>
                        )}
                    </button>
                    <button onClick={handleMute}>
                        {isMuted ? (
                            <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5v14M4.929 4.929l14.142 14.142M15 5v14M19.071 4.929l-14.142 14.142" />
                            </svg>
                        ) : (
                            <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5v14M4.929 4.929l14.142 14.142M15 5v14M19.071 4.929l-14.142 14.142" />
                            </svg>
                        )}
                    </button>

                    <input type="range" min="0" max="1" step="0.01" value={volume} onChange={handleVolumeChange} />
                </div>
                <div className="flex flex-row items-center">
                    <input type="range" min="0" max={duration} step="0.01" value={currentTime} onChange={handleTimeUpdate} />
                    <span className="text-white">{currentTime}</span>
                </div>
                <div className="flex flex-row items-center">
                    <select value={quality} onChange={handleQualityChange}>
                        <option value="0">Auto</option>
                        <option value="1">1080p</option>
                        <option value="2">720p</option>
                        <option value="3">480p</option>
                        <option value="4">360p</option>
                        <option value="5">240p</option>
                    </select>
                    <button onClick={handleFullscreen}>
                        {isFullscreen ? (
                            <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 10l7-7m0 0l7 7m-7-7v18m4-4h7a2 2 0 002-2V6a2 2 0 00-2-2h-7a2 2 0 00-2 2v7a2 2 0 002 2z" />
                            </svg>
                        ) : (
                            <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 16l-4-4m0 0l4-4m-4 4h18m-7 4l4 4m0 0l-4 4m4-4H3" />
                            </svg>
                        )}
                    </button>
                </div>
            </div>
        </div>
    )
}

