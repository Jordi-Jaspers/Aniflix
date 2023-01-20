import {MediaSource} from "@interfaces/MediaSource";
import {MediaSources} from "@interfaces/MediaSources";
import Hls from 'hls.js';
import React, {useEffect, useRef, useState} from 'react';

interface Props {
    className?: string;
    media: MediaSources;
    controls: boolean;
    innerRef: React.RefObject<HTMLVideoElement>;
}

const NATIVE_HLS: string = 'application/vnd.apple.mpegurl';
const DEFAULT_VIDEO: string = 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8';
export default function VideoPlayer({className, media, controls, innerRef}: Props) {
    const referer: string = media?.headers?.referer || '';
    const mediaSources: Map<string, string[]> = new Map();
    media?.sources?.forEach((source: MediaSource) => {
        if (source?.url) {
            if (mediaSources.has(source?.quality)) {
                mediaSources.get(source?.quality)?.push(source?.url);
            } else {
                mediaSources.set(source?.quality, [source?.url]);
            }
        }
    });
    
    const [currentResolution, setCurrentResolution] = useState(Array.from(mediaSources.keys())[0]);
    const [videoPlayer, setVideoPlayer] = useState<HTMLVideoElement | null>(null);
    
    useEffect(() => {
        setVideoPlayer(innerRef.current);
        if (!videoPlayer) return;
        videoPlayer.focus();
        videoPlayer.controls = controls;
        videoPlayer.autoplay = true;
        
        const url = mediaSources.get(currentResolution)?.[0] || DEFAULT_VIDEO;
        if (!url) return;
        
        if (videoPlayer.canPlayType(NATIVE_HLS)) {
            console.info('Using native HLS in browser');
            videoPlayer.src = url;
        } else if (Hls.isSupported()) {
            console.debug('Using HLS supported browser')
            const hls = new Hls();
            hls.loadSource(url);
            hls.attachMedia(videoPlayer);
            hls.on(Hls.Events.MEDIA_ATTACHED, function () {
                console.debug('video-tag and HLS.js are now bound.');
            });
        } else {
            console.error('HLS is not supported in this browser. Please use a supported browser.')
        }
    }, [videoPlayer]);
    
    return (
        <div className={`${className} w-full h-full`}>
            <div className={`absolute top-0 bottom-0 my-auto w-screen`}>
                <video className={"w-screen h-full max-h-screen"} ref={innerRef}/>
            </div>
        </div>
    );
}

