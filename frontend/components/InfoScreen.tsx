import MuiModal from '@mui/material/Modal'
import {useRecoilState} from "recoil";
import {animeState, infoScreenState} from "../atoms/AnimeAtom";
import {PlayIcon, XMarkIcon} from "@heroicons/react/24/solid";
import React, {useState} from "react";
import {HandThumbUpIcon, SpeakerWaveIcon, SpeakerXMarkIcon} from '@heroicons/react/24/outline';
import YouTube from "react-youtube";

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
    const [anime, setAnime] = useRecoilState(animeState)

    // Some hooks to handle the state of the banner with the video.
    const [player, setPlayer] = useState<any>(null)
    const [isMuted, setIsMuted] = useState(true)

    const onError = () => {
        console.error("Error while playing the trailer.")
        handleClose()
    }

    const onReady = (event: any) => {
        setPlayer(event.target)
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

    console.log(anime)

    return (
        <MuiModal open={showInfoScreen} onClose={handleClose}>
            <div className={"bg-[#181818] h-screen min-w-[850px] fixed !top-7 left-0 right-0 z-50 mx-auto w-full max-w-5xl overflow-hidden overflow-y-scroll rounded-md scrollbar-hide"}>
                <button
                    className={"absolute m-[1em] right-5 h-9 w-9 flex justify-center items-center top-5 bg-[#181818] rounded-full text-white !z-40"}
                    onClick={handleClose}>
                    <XMarkIcon className={"h-6 w-6"}/>
                </button>
                <div className={"bg-gradient-to-b from-black/10 via-black/95 to-[#181818] !z-40"}>
                    <div className="relative pt-[56.25%]">
                        <YouTube
                            videoId={anime?.trailer.id}
                            className={"absolute top-0 left-0 w-screen aspect-video object-cover -z-10"}
                            opts={opts}
                            onReady={onReady}
                            onError={onError}
                        />
                        <div className="absolute bottom-10 flex w-full items-center justify-between px-10">
                            <div className="flex items-center space-x-4">
                                <button className="bannerButton rounded bg-white text-black">
                                    <PlayIcon className="text-black h-4 w-4 md:h-6 md:w-6"/> Play
                                </button>
                                <button
                                    className="flex h-11 w-11 items-center justify-center rounded-full border-2 border-[gray] bg-[#2a2a2a]/60 transition hover:border-white">
                                    <HandThumbUpIcon className="text-white h-6 w-6"/>
                                </button>
                            </div>
                            <button
                                className={"flex h-11 w-11 items-center justify-center rounded-full border-2 border-[gray] bg-[#2a2a2a]/60 transition hover:border-white"}
                                onClick={toggleMute}>
                                {isMuted
                                    ? <SpeakerXMarkIcon className="h-6 w-6 text-white"/>
                                    : <SpeakerWaveIcon className="h-6 w-6 text-white"/>}
                            </button>
                        </div>
                    </div>
                    <div className="flex space-x-16 rounded-b-md px-10 py-8">
                        <div className="space-y-6 text-lg">
                            <div className="flex items-center space-x-2 text-sm">
                                <p className="text-[#46d369] text-lg !z-40">
                                    {anime?.rating? anime.rating : "0"} % match
                                </p>
                                <p className="text-white text-lg font-light !z-40">
                                    | {anime?.releaseDate ? anime?.releaseDate : anime?.startDate.year} |
                                </p>
                                <p className="text-white text-lg font-light !z-40">
                                    {anime?.totalEpisodes} Episodes
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
                            <div className="flex flex-col gap-x-10 gap-y-4 font-light md:flex-row">
                                <p className="text-white w-5/6 text-sm font-poppins">{anime?.description.replace(/<[^>]*>?/gm, '')}</p>
                                <div className="flex flex-col space-y-3 text-sm">
                                    <div className={"text-white"}>
                                        <span className="text-[gray]">Genres:</span>{' '}
                                        {anime?.genres.map((genre) => genre).join(', ')}
                                    </div>

                                    {anime?.countryOfOrigin ? (
                                        <div className={"text-white"}>
                                            <span className="text-[gray]">Country Of Origin:</span>
                                            {' '}
                                            {anime?.countryOfOrigin}
                                        </div>
                                    ) : null}

                                    <div className={"text-white"}>
                                        <span className="text-[gray]">Status:</span>{' '}
                                        {anime?.status}
                                    </div>

                                    <div className={"text-white"}>
                                        <span className="text-[gray]">Type:</span>{' '}
                                        {anime?.type}
                                    </div>

                                    <div className={"text-white"}>
                                        <span className="text-[gray]">Average Duration:</span>{' '}
                                        {anime?.duration} min
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="flex flex-row justify-between space-x-16 rounded-b-md px-10 py-8">
                    <h4 className={"infoScreenEpisode"}>Episodes</h4>
                    <p className={"infoScreenEpisode text-lg py-1.5 px-8 bg-[#242424] border-[#4d4d4d] border rounded "}>Total: {anime?.totalEpisodes}</p>
                </div>
            </div>

        </MuiModal>
    )
}
