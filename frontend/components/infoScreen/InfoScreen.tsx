import {infoScreenState} from "@atoms/InfoScreenAtom";
import {animeState} from "@atoms/VideoPlayerAtom";
import EpisodeCards from "@components/infoScreen/EpisodeCards";
import MetaInformation from "@components/infoScreen/MetaInformation";
import RecommendationCards from "@components/infoScreen/RecommendationCards";
import {SpeakerWaveIcon, SpeakerXMarkIcon} from '@heroicons/react/24/outline';
import {PlayIcon, XMarkIcon} from "@heroicons/react/24/solid";
import AddToLibraryIcon from "@icons/AddToLibraryIcon";
import LikeButtonIcon from "@icons/LikeButtonIcon";
import MuiModal from '@mui/material/Modal'
import Image from "next/image";
import React, {useState} from "react";
import YouTube from "react-youtube";
import {useRecoilState, useRecoilValue} from "recoil";

const opts = {
    height: '100%',
    width: '100%',
    playerVars: {
        'autoplay': 1,
        'controls': 0,
        'https': 1,
        'wmode': 'opaque',
        'mute': 1,
        'loop': 1,
    },
};

export default function InfoScreen() {
    // Atoms which can be accessed by any component.
    const [showInfoScreen, setShowInfoScreen] = useRecoilState(infoScreenState)
    const anime = useRecoilValue(animeState)
    
    // Some hooks to handle the state of the banner with the video.
    const [player, setPlayer] = useState<any>(null)
    const [isMuted, setIsMuted] = useState(true)
    const [isError, setIsError] = useState(false)
    const [isPlaying, setIsPlaying] = useState(false)
    
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
    
    const handleClose = () => {
        setShowInfoScreen(false)
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
    
    return (
        <>
            {anime && (
                <MuiModal open={showInfoScreen} onClose={handleClose} className={"flex justify-center py-4"}>
                    <div
                        className={"bg-[#1a1920] fixed !top-7 !bottom-7 z-50 mx-auto h-auto w-full max-w-4xl overflow-hidden overflow-y-scroll scrollbar-hide rounded-md"}>
                        <button
                            className={"absolute m-[1em] top-0 right-0 h-9 w-9 flex justify-center items-center bg-[#1a1920]/80 hover:bg-[#1a1920] rounded-full text-white !z-40 onclick"}
                            onClick={handleClose}>
                            <XMarkIcon className={"h-6 w-6"}/>
                        </button>
                        <div className={"!z-40"}>
                            <div className="relative pt-[56.25%]">
                                <Image
                                    src={anime.cover as string}
                                    alt={(anime.title.romaji ? anime.title.romaji : anime.title.english) as string}
                                    height={2160}
                                    width={3840}
                                    className={`${!isPlaying || isError ? "info-banner-gradient brightness-85 absolute top-0 left-0 w-full aspect-video object-cover -z-10" : "hidden"}`}
                                />
                                
                                {anime.trailer?.id && (
                                    <YouTube
                                        videoId={anime.trailer.id}
                                        className={`${isPlaying && !isError ? "info-banner-gradient brightness-85 absolute top-0 left-0 w-full aspect-video object-cover -z-10" : "hidden"}`}
                                        opts={opts}
                                        onReady={onReady}
                                        onError={onError}
                                        onEnd={onEnd}
                                        onPlay={onPlay}
                                    />
                                )}
                                <div className="absolute bottom-10 flex w-full items-center justify-between px-10">
                                    <div className="flex items-center space-x-2">
                                        <button className="bannerButton rounded bg-white text-black">
                                            <PlayIcon className="text-black h-8 w-8"/> Play
                                        </button>
                                        <AddToLibraryIcon anime_id={anime.id.toString()} buttonClassName={"h-10 w-10"} iconClassName={"h-6 w-6"}/>
                                        <LikeButtonIcon buttonClassName={"h-10 w-10"} iconClassName={"h-6 w-6"}/>
                                    </div>
                                    {anime?.trailer &&
                                        <button
                                            className={`${isPlaying ? "flex h-11 w-11 items-center justify-center rounded-full border-2 border-[gray] bg-[#2a2a2a]/60 transition hover:border-white" : "hidden"}`}
                                            onClick={toggleMute}>
                                            {isMuted
                                                ? <SpeakerXMarkIcon className="h-8 w-8 text-white"/>
                                                : <SpeakerWaveIcon className="h-8 w-8 text-white"/>}
                                        </button>
                                    }
                                </div>
                            </div>
                            <div className="space-y-8 rounded-b-md px-10 py-8">
                                <div className="space-y-6 text-lg w-full flex-col justify-center">
                                    <div className="flex items-center space-x-2 text-sm">
                                        <p className="text-[#46d369] text-lg !z-40">
                                            {anime?.rating ? anime.rating : "0"}% match
                                        </p>
                                        <p className="text-white text-lg font-light !z-40">
                                            - {anime?.releaseDate ? anime?.releaseDate : anime?.startDate.year}
                                        </p>
                                        <p className="text-white text-lg font-light !z-40">
                                            - {anime?.totalEpisodes} Episodes
                                        </p>
                                        <div
                                            className="flex text-white h-4 items-center justify-center rounded border border-white/40 px-1.5 text-xs !z-40">
                                            HD
                                        </div>
                                    </div>
                                    <h1 className="text-3xl font-bold text-white">
                                        {anime?.title.romaji ? anime.title.romaji : anime?.title.english}
                                        {anime?.subOrDub ? ` (${anime.subOrDub})` : ""}
                                    </h1>
                                    <div className="flex flex-col gap-x-10 gap-y-4 font-light w-full md:flex-row">
                                        <p className="text-white w-full md:w-[70%] text-base leading-7 font-poppins text-justify">{anime?.description.replace(/<[^>]*>?/gm, '')}</p>
                                        <div className="flex flex-col text-sm">
                                            <div className={"metaInfoTitle"}>
                                                <span className="text-[gray]">Genres:</span>{' '}
                                                {anime?.genres.map((genre) => genre).join(', ')}
                                            </div>
                                            
                                            <div className={"metaInfoTitle"}>
                                                <span className="text-[gray]">Season:</span>{' '}
                                                {anime.season}
                                            </div>
                                            
                                            <div className={"metaInfoTitle"}>
                                                <span className="text-[gray]">Status:</span>{' '}
                                                {anime?.status}
                                            </div>
                                            
                                            <div className={"metaInfoTitle"}>
                                                <span className="text-[gray]">Type:</span>{' '}
                                                {anime?.type}
                                            </div>
                                            
                                            <div className={"metaInfoTitle"}>
                                                <span className="text-[gray]">Average Duration:</span>{' '}
                                                {anime?.duration} min
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className={"flex flex-col space-y-8 px-10 pt-4"}>
                            {/* The List with all the available episodes of the selected anime */}
                            {anime?.episodes && (<EpisodeCards episodes={anime.episodes}/>)}
                            
                            {/* The List with all the recommended series of the selected anime */}
                            {anime?.recommendations && (<RecommendationCards recommendations={anime?.recommendations}/>)}
                            
                            {/* Some Meta information about the clicked anime */}
                            <MetaInformation anime={anime}/>
                        </div>
                    </div>
                </MuiModal>
            )}
        </>
    )
}
