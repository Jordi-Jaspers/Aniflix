import {RecentEpisode} from "@interfaces/RecentEpisode";
import AnimeService from "@util/consumet/AnimeService";
import Image from 'next/image'
import {Router, useRouter} from "next/router";
import {useRecoilState} from 'recoil'
import {Anime, hasAllAnimeProperties} from "@interfaces/Anime";
import {animeState, infoScreenState} from "@atoms/AnimeAtom";
import {useState} from 'react';

interface Props {
    anime: Anime | RecentEpisode;
}

export default function Thumbnail({anime}: Props) {
    const router = useRouter()
    const [showInfoScreen, setShowInfoScreen] = useRecoilState(infoScreenState)
    const [currentAnime, setCurrentAnime] = useRecoilState(animeState)
    const [isShown, setIsShown] = useState(false);

    const videoTitle = (): string => {
        let title: string = anime.title.romaji ? anime.title.romaji : anime.title.english;
        return anime.hasOwnProperty('episodeNumber') ? title + " (Episode " + anime.episodeNumber + ")" : title;
    };

    const rating = (): string => {
        if (anime.hasOwnProperty('rating') && "rating" in anime && anime.rating !== null) {
            return anime.rating + '%';
        }
        return '0%';
    }

    const handleClickedAnime = async () => {
        if (anime.hasOwnProperty('episodeId') && anime.hasOwnProperty('id')) {
            const episode: RecentEpisode = anime as RecentEpisode;
            await router.push('/watch/[anime_id]/[episode_id]', `/watch/${episode.id}/${episode.episodeNumber}`)
        } else {
            const id: string = anime.id.toString();
            if (hasAllAnimeProperties(anime)) {
                setCurrentAnime(anime);
                setShowInfoScreen(true);
            } else {
                const details: Anime = await AnimeService.getAnimeDetails(id);
                setCurrentAnime(details);
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
                <div
                    className={`${isShown ? "opacity-80" : "opacity-30"} transition duration-300 w-fit min-w-[2rem] min-h-[1rem] rounded py-0.5 px-1.5 border-[#ce111c] bg-[#ce111c]`}>
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
