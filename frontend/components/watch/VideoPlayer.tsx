import {currentTimeState, sourceState, videoPlayerState} from "@atoms/VideoPlayerAtom";
import {LOGGER} from "@util/Logger";

import React, {useEffect, useState} from 'react';

import Hls from 'hls.js';
import {useRecoilValue, useSetRecoilState} from "recoil";



interface Props {
    controls?: boolean;
    loop?: boolean;
    innerRef: React.RefObject<HTMLVideoElement>;
}

const NATIVE_HLS: string = 'application/vnd.apple.mpegurl';
export default function VideoPlayer({controls, loop, innerRef}: Props) {
    const [videoPlayer, setVideoPlayer] = useState<HTMLVideoElement | null>(null);
    const {isPlaying, streamingLinks, resolution, source} = useRecoilValue(videoPlayerState);
    const setCurrentTime = useSetRecoilState(currentTimeState);
    const setSource = useSetRecoilState(sourceState);
    
    useEffect(() => {
        LOGGER.debug('[VideoPlayer] Setting up video player');
        setVideoPlayer(innerRef.current);
        if (!videoPlayer) return;
        
        const url = streamingLinks?.sources.find(source => source.quality === resolution)?.url;
        if (url && source !== url) {
            LOGGER.debug(`[VideoPlayer] Setting source to ${url} with quality ${resolution}`);
            setCurrentTime(videoPlayer.currentTime)
            setSource(url);
        }
        
        videoPlayer.focus();
        videoPlayer.autoplay = isPlaying;
        videoPlayer.controls = controls ?? false;
        videoPlayer.loop = loop ?? false;
        if (videoPlayer.canPlayType(NATIVE_HLS)) {
            LOGGER.debug('[VideoPlayer] Using native HLS in browser');
            videoPlayer.src = source;
        } else if (Hls.isSupported()) {
            LOGGER.debug('[VideoPlayer] Using HLS supported browser')
            const hls = new Hls();
            hls.loadSource(source);
            hls.attachMedia(videoPlayer);
            hls.on(Hls.Events.MEDIA_ATTACHED, function () {
                LOGGER.debug('[VideoPlayer] video-tag and HLS.js are now bound.');
            });
            return () => hls.destroy();
        } else {
            LOGGER.error('[VideoPlayer] HLS is not supported in this browser. Please use a supported browser.')
        }
    }, [videoPlayer, resolution, source]);
    
    return (
        <div className={`w-full h-full`}>
            <div className={`absolute top-0 bottom-0 my-auto w-screen`}>
                <video className={"w-screen h-full max-h-screen"} ref={innerRef}/>
            </div>
        </div>
    );
}

