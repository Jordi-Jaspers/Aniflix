import Image from 'next/image'
import {useRecoilState} from 'recoil'
import {GoEpisode} from "../../interfaces/GoEpisode";
import {Anime, hasAllProperties, isAnime} from "../../interfaces/Anime";
import {animeState, infoScreenState, videoPlayerState} from "../../atoms/AnimeAtom";
import {useEffect, useState} from 'react';
import consumetApi from "../../utils/ConsumetApi";
import ConsumetApi from "../../utils/ConsumetApi";

interface Props {
    anime: Anime | GoEpisode
}

export default function Thumbnail({anime}: Props) {
    // Atoms which can be accessed by any component.
    const [showInfoScreen, setShowInfoScreen] = useRecoilState(infoScreenState)
    const [currentAnime, setCurrentAnime] = useRecoilState(animeState)

    const [isShown, setIsShown] = useState(false);

    const videoTitle = (): string => {
        if (anime.hasOwnProperty('title')) {
            if (typeof anime.title === 'object') {
                return anime.title.romaji ? anime.title.romaji : anime.title.english;
            } else if (anime.hasOwnProperty('episodeNumber')) {
                return anime.title + " (Episode " + anime.episodeNumber + ")";
            }
        }
        return 'Error retrieving title';
    };

    const rating = (): string => {
        if (anime.hasOwnProperty('rating') && "rating" in anime && anime.rating !== null) {
            return anime.rating + '%';
        }
        return '0%';
    }

    const handleClickedAnime = async () => {
        // Check whether the clicked thumbnail has all the properties needed for the info screen.
        if (hasAllProperties(anime)) {
            setCurrentAnime(anime as Anime);
            setShowInfoScreen(true);
        } else {
            if (anime.hasOwnProperty('episodeId')) {
                // Retrieve the details of the go-anime episode.
                // TODO: play video.
            } else {
                console.log(consumetApi.fetchAnimeDetails);
                const json = await fetch(ConsumetApi.fetchAnimeDetails.replace('{id}', anime.id)).then((res) => res.json());
                setCurrentAnime(json);
                setShowInfoScreen(true);
            }
        }
    }

    return (
        <div
            className={`relative h-28 min-w-[180px] cursor-pointer transition duration-200 ease-out md:h-36 md:min-w-[260px] md:hover:scale-105`}
            onClick={() => {handleClickedAnime()}}
            onMouseEnter={() => setIsShown(true)}
            onMouseLeave={() => setIsShown(false)}
        >
            {/* add a gradient over the image with the title */}
            <div className="absolute top-0 left-0 w-full h-full bg-gradient-to-t from-black/90 via-transparent to-transparent z-20"/>
            <div className="relative flex flex-col justify-between items-end p-2 w-full h-full justify-center z-20">
                <div className={`${isShown ? "opacity-80" : "opacity-30"} transition duration-300 w-fit min-w-[2rem] min-h-[1rem] rounded py-0.5 px-1.5 border-[#ce111c] bg-[#ce111c]`}>
                    <p className={`font-poppins leading-none justify-center text-sm text-[#e5e5e5] text-shadow font-bold text-center`}>{rating()}</p>
                </div>
                <p className="font-poppins w-full leading-none text-sm text-[#e5e5e5] font-bold">{videoTitle()}</p>
            </div>
            <Image
                src={anime.image}
                alt={videoTitle()}
                className="rounded-sm object-cover md:rounded"
                fill={true}
                sizes="(max-width: 768px) 100vw, 50vw"
            />
        </div>
    )
}
