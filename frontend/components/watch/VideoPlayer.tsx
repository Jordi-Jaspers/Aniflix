import {MediaSources} from "@interfaces/MediaSources";
import {MediaSource} from "@interfaces/MediaSource";
import Hls from 'hls.js';
import React, {useEffect, useRef} from 'react';

interface Props {
    media: MediaSources;
}

export default function VideoPlayer({media}: Props) {
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
        <video ref={videoSource} controls={true}>
            Your browser does not support the video tag.
        </video>
    );
}
