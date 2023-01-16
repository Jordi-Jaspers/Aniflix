import CustomControls from "@components/watch/CustomControls";
import {MediaSources} from "@interfaces/MediaSources";
import {MediaSource} from "@interfaces/MediaSource";
import Hls from 'hls.js';
import React, {useEffect, useRef} from 'react';

interface Props {
    className?: string;
    media: MediaSources;
    controls: boolean;
}

export default function VideoPlayer({className, media, controls}: Props) {
    const videoSource = useRef<HTMLVideoElement>(null);
    const mediaSources: MediaSource[] = media?.sources || [];
    const referer: string = media?.headers?.referer || '';

    useEffect(() => {
        if (mediaSources.length > 0) {
            mediaSources.forEach((mediaSource: MediaSource) => {
                const source = document.createElement('source');
                source.src = mediaSource.url;
                source.type = 'application/x-mpegURL';
                source.setAttribute("data-res", mediaSource.quality);
                videoSource.current.appendChild(source);
            });
        }

        if (Hls.isSupported()) {
            const hls = new Hls({
                xhrSetup: xhr => {
                    xhr.setRequestHeader("Referer", referer);
                }
            })

            hls.loadSource(mediaSources[0].url);
            hls.attachMedia(videoSource.current);
            hls.on(Hls.Events.MANIFEST_PARSED, () => {
                videoSource.current.play();
            });
            hls.on(Hls.Events.LEVEL_LOADED, (event, data) => {
                const currentRes = hls.levels[data.level].attrs.RESOLUTION;
                const sources = videoSource.current.getElementsByTagName("source");
                for (let i = 0; i < sources.length; i++) {
                    const source = sources[i] as HTMLSourceElement;
                    if (source.getAttribute("data-res") === currentRes) {
                        source.src = hls.levels[data.level].url;
                        source.type = 'application/x-mpegURL';
                        videoSource.current.src = source.src;
                    }
                }
            });
        }
    }, [mediaSources]);

    return (
        <div className={`${className} w-full h-full`}>
            {/*<video ref={videoSource} controls={false}/>*/}
            {/*{controls && (<CustomControls ref={videoSource}/>)}*/}
        </div>
    );
}
