import CustomControls from "@components/watch/CustomControls";
import {Anime} from "@interfaces/Anime";
import {Episode} from "@interfaces/Episode";
import {MediaSource} from "@interfaces/MediaSource";
import {MediaSources} from "@interfaces/MediaSources";
import Hls from 'hls.js';
import React, {useEffect, useRef, useState} from 'react';

interface Props {
    className?: string;
    media: MediaSources;
    episode: Episode;
    controls: boolean;
}

const NATIVE_HLS: string = 'application/vnd.apple.mpegurl';
export default function VideoPlayer({className, media, episode, controls}: Props) {
    const [isShown, setIsShown] = useState(false);
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
    const videoElement = useRef<HTMLVideoElement>(null);

    useEffect(() => {
        const videoRef = videoElement.current
        if (!videoRef) return;

        const url = mediaSources.get(currentResolution)[0];
        if (!url) return;

        if (videoRef.canPlayType(NATIVE_HLS)) {
            console.info('Using native HLS in browser');
            videoRef.src = url;
        } else if (Hls.isSupported()) {
            console.log('Using HLS supported browser')
            const hls = new Hls({
                xhrSetup: xhr => {
                    xhr.setRequestHeader("Referer", referer);
                }
            })
            hls.loadSource(url);
            hls.attachMedia(videoElement.current);
            hls.on(Hls.Events.MANIFEST_PARSED, () => {
                videoElement.current?.play();
            });
            return () => hls.destroy();
        } else {
            console.error('HLS is not supported in this browser. Please use a supported browser.')
        }
    }, [mediaSources, videoElement])

    return (
        <div className={`${className} w-full h-full`}>
            <div className={`absolute top-0 bottom-0 my-auto w-screen`}>
                <video className={"w-screen h-full max-h-screen"} ref={videoElement} controls={false} muted={true}/>
                {/* if the "controls == true && videoElement != undefined" then show the customControls */}
                {controls && <CustomControls className={"hover:opacity-100 opacity-0"} ref={videoElement} episode={episode}/>}
            </div>
        </div>
    );
}
