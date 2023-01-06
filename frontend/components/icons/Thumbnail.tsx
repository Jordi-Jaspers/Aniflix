import Image from 'next/image'
import {useRecoilState} from 'recoil'
import {GoEpisode} from "../../interfaces/GoEpisode";
import {Anime} from "../../interfaces/Anime";
import {animeState, modalState} from "../../atoms/modalAtom.";
import {useState} from 'react';

interface Props {
    anime: Anime | GoEpisode
}

export default function Thumbnail({anime}: Props) {
    const [currentAnime, setCurrentAnime] = useRecoilState(animeState)
    const [showModal, setShowModal] = useRecoilState(modalState)
    const [isShown, setIsShown] = useState(false);

    const videoTitle = (anime: Anime | GoEpisode): string => {
        if (anime.hasOwnProperty('title')) {
            if (typeof anime.title === 'object') {
                return anime.title.romaji ? anime.title.romaji : anime.title.english;
            } else {
                return anime.title;
            }
        }
        return 'Error retrieving title';
    };

    const rating = (anime: Anime | GoEpisode): string => {
        if (anime.hasOwnProperty('rating') && "rating" in anime && anime.rating !== null) {
            return anime.rating + '%';
        }
        return '0%';
    }


    return (
        <div
            className={`relative h-28 min-w-[180px] cursor-pointer transition duration-200 ease-out md:h-36 md:min-w-[260px] md:hover:scale-105`}
            onClick={() => {
                setCurrentAnime(anime)
                setShowModal(true)
            }}
            onMouseEnter={() => setIsShown(true)}
            onMouseLeave={() => setIsShown(false)}
        >
            {/* add a gradient over the image with the title */}
            <div className="absolute top-0 left-0 w-full h-full bg-gradient-to-t from-black/90 via-transparent to-transparent z-20"/>
            <div className="relative flex flex-col justify-between items-end p-2 w-full h-full justify-center z-20">
                <div className={`${isShown ? "opacity-80" : "opacity-30"} transition duration-300 w-fit min-w-[2rem] min-h-[1rem] rounded border-[#ce111c] bg-[#ce111c]`}>
                    <p className={`font-poppins leading-none justify-center text-sm text-[#e5e5e5] text-shadow font-bold text-center`}>{rating(anime)}</p>
                </div>
                <p className="font-poppins w-full leading-none text-sm text-[#e5e5e5] font-bold">{videoTitle(anime)}</p>
            </div>
            <Image
                src={anime.image}
                alt={videoTitle(anime)}
                className="rounded-sm object-cover md:rounded"
                layout="fill"
                sizes="(max-width: 768px) 100vw, 50vw"
            />
        </div>
    )
}
