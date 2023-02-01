import {infoScreenState} from "@atoms/InfoScreenAtom";
import {animeState} from "@atoms/VideoPlayerAtom";
import AnimeService from "@consumet/AnimeService";
import {InformationCircleIcon, SpeakerWaveIcon, SpeakerXMarkIcon} from '@heroicons/react/24/outline'
import {PlayIcon} from '@heroicons/react/24/solid'
import {Anime, hasAllAnimeProperties} from "@interfaces/Anime";
import Image from 'next/image'
import React, {useEffect, useState} from 'react';
import YouTube from 'react-youtube';
import {useRecoilState, useSetRecoilState} from "recoil";

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

interface Props {
    anime: Anime
}

export default function Banner({anime}: Props) {
    // Atoms which can be accessed by any component.
    const [showInfoScreen, setShowInfoScreen] = useRecoilState(infoScreenState)
    const setClickedAnime = useSetRecoilState(animeState)
    
    // Some hooks to handle the state of the banner with the video.
    const [player, setPlayer] = useState<any>(null)
    const [isMuted, setIsMuted] = useState(true)
    const [isError, setIsError] = useState(false)
    const [isPlaying, setIsPlaying] = useState(false)
    
    const videoTitle: string = anime.title.romaji ? anime.title.romaji : anime.title.english
    
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
        if (anime.hasOwnProperty('id')) {
            const id: string = anime.id.toString();
            if (hasAllAnimeProperties(anime)) {
                setShowInfoScreen(true)
                setClickedAnime(anime)
            } else {
                const details: Anime = await AnimeService.getAnimeDetails(id)
                setShowInfoScreen(true)
                setClickedAnime(details)
            }
        }
    }
    
    return (
        <div className="w-screen flex flex-col space-y-2 ht-[56.25vw] h-[40vw] md:space-y-4 pl-4 pr-4 md:pl-6 md:pr-6 lg:pl-12 lg:pr-12">
            <div className={"absolute aspect-video min-h-[112.25vw] w-full top-0 left-0"}>
                <Image
                    src={anime.cover}
                    alt={videoTitle}
                    height={2160}
                    width={3840}
                    className={`${!isPlaying || isError ? "brightness-75 absolute max-h-[50%] h-full w-full object-cover !-z-20" : "hidden"}`}
                    priority
                />
                
                {anime?.trailer?.id && (
                    <YouTube
                        videoId={anime.trailer.id}
                        className={`${isPlaying && !isError ? "brightness-75 absolute max-h-[50%] h-full w-full object-cover !-z-20" : "hidden"}`}
                        opts={opts}
                        onReady={onReady}
                        onError={onError}
                        onEnd={onEnd}
                        onPlay={onPlay}
                    />
                )}
                <div className={"relative z-[-10] w-full mt-[30%] h-[30%] bg-gradient-to-b from-transparent via-black to-[#141414]"}/>
            </div>
            
            <div className={"z-10 h-full space-y-4 flex flex-col justify-end pb-[5%]"}>
                <div className={"flex flex-col space-y-2 justify-between"}>
                    {videoTitle.length > 40
                        ?
                        <h1 className="font-poppins font-bold text-[#fefefe] max-w-[50%] leading-none text-[3.0vw]">{videoTitle}</h1>
                        :
                        <h1 className="font-poppins font-bold text-[#fefefe] max-w-[50%] leading-none text-[3.5vw]">{videoTitle}</h1>
                    }
                    <div className="font-poppins text-[#fefefe] md:text-[1.2vw] text-shadow-md max-w-xs md:max-w-lg lg:max-w-xl">
                        {(anime.description.length > 350)
                            ? (<p className={"text-[1.2vw]"}>{anime.description.replace(/<[^>]*>?/gm, '')}</p>)
                            : (<p className={"text-[1.75vw]"}>{anime.description.replace(/<[^>]*>?/gm, '')}</p>)
                        }
                    </div>
                </div>
                
                <div className="z-10 flex flex-row justify-between w-full">
                    <div className="flex space-x-3">
                        <button className="bannerButton rounded bg-white text-black">
                            <PlayIcon className="text-black h-4 w-4 md:h-6 md:w-6"/> Play
                        </button>
                        
                        <button className="bannerButton bg-[#4f4f50] opacity-75 text-white hover:opacity-100"
                                onClick={handleInfoScreen}>
                            <InformationCircleIcon className="h-4 w-4 md:h-6 md:w-6"/> More Information
                        </button>
                    </div>
                    <div className="flex space-x-3">
                        <button
                            className={`${isPlaying ? "items-center border-2 border-[gray] hover:border-white rounded-full p-2 bg-[#4f4f50] text-white opacity-50 hover:opacity-100 z-20" : "hidden"}`}
                            onClick={toggleMute}>
                            {isMuted
                                ? <SpeakerXMarkIcon className="h-4 w-4 md:h-6 md:w-6"/>
                                : <SpeakerWaveIcon className="h-4 w-4 md:h-6 md:w-6"/>
                            }
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}
