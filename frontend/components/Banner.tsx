import {InformationCircleIcon, SpeakerWaveIcon, SpeakerXMarkIcon} from '@heroicons/react/24/outline'
import {PlayIcon} from '@heroicons/react/24/solid'
import Image from 'next/image'
import React, {useState} from 'react';
import YouTube, {YouTubeProps} from 'react-youtube';
import {Anime} from "../interfaces/Anime";

interface Props {
    randomAnime: Anime
}

const opts = {
    height: '110%',
    width: '100%',
    playerVars: {
        'autoplay': 1,
        'controls': 0,
        'https': 1,
        'wmode': 'opaque',
    },
};

export default function Banner({randomAnime}: Props) {
    const [player, setPlayer] = useState<any>(null)
    const [muted, setMuted] = useState(false)
    const [isReady, setIsReady] = useState(false)
    const [isPlaying, setIsPlaying] = useState(false)

    const onError = () => {
        setIsReady(false)
    }

    const onReady: YouTubeProps['onReady'] = (event) => {
        setPlayer(event.target)
        setIsReady(true)
        player?.playVideo()
    }

    const onPlay = () => {
        setIsPlaying(true)
    }

    const onEnd: YouTubeProps['onEnd'] = () => {
        setIsPlaying(false)
    }

    const toggleMute = () => {
        if (muted) {
            player?.unMute()
            setMuted(false)
        } else {
            player?.mute()
            setMuted(true)
        }
    }

    const videoTitle: string = randomAnime.title.romaji ? randomAnime.title.romaji : randomAnime.title.english
    return (
        <div className="flex flex-col space-y-2 ht-[56.25vw] md:space-y-4 pl-4 pr-4 md:pl-6 md:pr-6 lg:pl-12 lg:pr-12">
            <div className={`${isPlaying && isReady ? "hidden" : "absolute w-screen top-0 left-0 -z-20"}`}>
                <Image
                    src={randomAnime.cover}
                    alt={randomAnime.title.romaji ? randomAnime.title.romaji : randomAnime.title.english}
                    height={2160}
                    width={3840}
                    className="max-h-screen aspect-video object-cover filter brightness-[70%]"
                />
            </div>

            <div className={`${isPlaying && isReady ? "absolute w-screen top-0 left-0 -z-10" : "hidden"}`}>
                <YouTube
                    videoId={randomAnime.trailer.id}
                    className={"absolute z-10 top-0 left-0 w-screen aspect-video object-cover max-h-[51.25vw]"}
                    opts={opts}
                    onReady={onReady}
                    onError={onError}
                    onEnd={onEnd}
                    onPlay={onPlay}
                />
            </div>

            <div className={"pt-[7.5vh] md:pt-[12.5vh] xl:pt-[15vh] 2xl:pt-[20vh] flex flex-col space-y-2 justify-between"}>
                {videoTitle.length > 40
                    ?
                    <h1 className="font-poppins font-bold text-[#fefefe] max-w-[50%] leading-none text-[3.0vw]">{videoTitle}</h1>
                    :
                    <h1 className="font-poppins font-bold text-[#fefefe] max-w-[50%] leading-none text-[3.5vw]">{videoTitle}</h1>
                }
                <p className="font-poppins text-[#fefefe] text-shadow-md max-w-xs text-[1.75vw] md:text-[1.2vw] md:max-w-lg lg:max-w-xl">
                    {(randomAnime.description.length > 350)
                        ? randomAnime.description.replace(/<[^>]*>?/gm, '').substring(0, 350) + "..."
                        : randomAnime.description.replace(/<[^>]*>?/gm, '')
                    }
                </p>
            </div>

            <div className="flex flex-row justify-between w-full">
                <div className="flex space-x-3">
                    <button className="bannerButton rounded bg-white text-black h-fit w-fit">
                        <PlayIcon className="text-black h-4 w-4 md:h-6 md:w-6"/> Play
                    </button>

                    <button className="bannerButton bg-[#4f4f50] opacity-75 text-white">
                        <InformationCircleIcon className="h-4 w-4 md:h-6 md:w-6"/> More Information
                    </button>
                </div>
                <div className="flex">
                    <button
                        className={`${isPlaying ? "items-center rounded-full px-2 bg-[#4f4f50] text-white opacity-50 hover:opacity-100" : "hidden"}`}
                        onClick={toggleMute}>
                        {muted ? <SpeakerXMarkIcon className="h-4 w-4 md:h-6 md:w-6"/> :
                            <SpeakerWaveIcon className="h-4 w-4 md:h-6 md:w-6"/>}
                    </button>
                </div>
            </div>
        </div>
    )
}
