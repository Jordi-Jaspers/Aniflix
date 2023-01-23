import {infoScreenState} from "@atoms/InfoScreenAtom";
import {animeState} from "@atoms/VideoPlayerAtom";
import {InformationCircleIcon, SpeakerWaveIcon, SpeakerXMarkIcon} from '@heroicons/react/24/outline'
import {PlayIcon} from '@heroicons/react/24/solid'
import {Anime, hasAllAnimeProperties} from "@interfaces/Anime";
import AnimeService from "@consumet/AnimeService";
import Image from 'next/image'
import React, {useEffect, useState} from 'react';
import YouTube from 'react-youtube';
import {useRecoilState, useSetRecoilState} from "recoil";

interface Props {
    randomAnime: Anime
}

const opts = {
    height: '100%',
    width: '100%',
    playerVars: {
        'autoplay': 1,
        'controls': 0,
        'https': 1,
        'wmode': 'opaque',
        'mute': 1,
    },
};

export default function Banner({randomAnime}: Props) {
    // Atoms which can be accessed by any component.
    const [showInfoScreen, setShowInfoScreen] = useRecoilState(infoScreenState)
    const setClickedAnime = useSetRecoilState(animeState)
    
    // Some hooks to handle the state of the banner with the video.
    const [player, setPlayer] = useState<any>(null)
    const [isMuted, setIsMuted] = useState(true)
    const [isError, setIsError] = useState(false)
    const [isPlaying, setIsPlaying] = useState(false)
    
    const videoTitle: string = randomAnime.title.romaji ? randomAnime.title.romaji : randomAnime.title.english
    
    useEffect(() => {
        if (showInfoScreen && isPlaying) {
            player?.pauseVideo()
            setIsPlaying(false)
        }
    }, [showInfoScreen])
    
    const onReady = (event: any) => {
        setPlayer(event.target)
    }
    
    const onPlay = () => {
        setIsPlaying(true)
    }
    
    const onError = () => {
        setIsError(true)
    }
    
    const onEnd = () => {
        setIsPlaying(false)
    }
    
    const toggleMute = () => {
        if (isMuted) {
            player?.unMute()
            setIsMuted(false)
        } else {
            player?.mute()
            setIsMuted(true)
        }
    }
    
    const handleInfoScreen = async () => {
        if (randomAnime.hasOwnProperty('id')) {
            const id: string = randomAnime.id.toString();
            if (hasAllAnimeProperties(randomAnime)) {
                setShowInfoScreen(true)
                setClickedAnime(randomAnime)
            } else {
                const details: Anime = await AnimeService.getAnimeDetails(id)
                setShowInfoScreen(true)
                setClickedAnime(details)
            }
        }
    }
    
    return (
        <div className="w-screen flex flex-col space-y-2 ht-[56.25vw] md:space-y-4 pl-4 pr-4 md:pl-6 md:pr-6 lg:pl-12 lg:pr-12">
            <div className={"absolute h-[65vw] sm:h-[60vw] lg:h-[45vw] w-full top-0 left-0"}>
                <Image
                    src={randomAnime.cover}
                    alt={randomAnime.title.romaji ? randomAnime.title.romaji : randomAnime.title.english}
                    height={2160}
                    width={3840}
                    className={`${!isPlaying || isError ? "brightness-75 absolute h-[90%] w-full object-cover -z-10" : "hidden"}`}
                    priority
                />
                
                {randomAnime?.trailer?.id && (
                    <YouTube
                        videoId={randomAnime.trailer.id}
                        className={`${isPlaying && !isError ? "brightness-75 absolute h-[90%] w-full object-cover -z-10" : "hidden"}`}
                        opts={opts}
                        onReady={onReady}
                        onError={onError}
                        onEnd={onEnd}
                        onPlay={onPlay}
                    />
                )}
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
                    <button className="bannerButton rounded bg-white text-black">
                        <PlayIcon className="text-black h-4 w-4 md:h-6 md:w-6"/> Play
                    </button>
                    
                    <button className="bannerButton bg-[#4f4f50] opacity-75 text-white"
                            onClick={handleInfoScreen}>
                        <InformationCircleIcon className="h-4 w-4 md:h-6 md:w-6"/> More Information
                    </button>
                </div>
                <div className="flex space-x-3">
                    <button
                        className={`${isPlaying ? "items-center rounded-full p-2 bg-[#4f4f50] text-white opacity-50 hover:opacity-100 z-20" : "hidden"}`}
                        onClick={toggleMute}>
                        {isMuted
                            ? <SpeakerXMarkIcon className="h-4 w-4 md:h-6 md:w-6"/>
                            : <SpeakerWaveIcon className="h-4 w-4 md:h-6 md:w-6"/>
                        }
                    </button>
                </div>
            </div>
        </div>
    )
}
