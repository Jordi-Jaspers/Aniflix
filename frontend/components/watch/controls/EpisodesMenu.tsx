import {episodeState} from "@atoms/VideoPlayerAtom";
import {QueueListIcon} from "@heroicons/react/24/outline";
import {Episode} from "@interfaces/Episode";
import {LOGGER} from "@util/Logger";
import Image from "next/image";
import router from "next/router";
import React, {useState} from 'react';
import {useRecoilValue} from "recoil";

interface Props {
    className?: string;
    episodes: Episode[];
}

export default function EpisodesMenu({className, episodes}: Props) {
    const [showList, setShowList] = useState(false);
    const selectedEpisode = useRecoilValue(episodeState);
    
    const handleClickedEpisode = async (episode_id: number) => {
        const {anime_id} = router.query;
        LOGGER.info(`[EpisodesMenu] Going to episode ${episode_id} of ${episodes.length}`);
        await router.push('/watch/[anime_id]/[episode_id]', `/watch/${anime_id}/${episode_id}`);
    }
    
    return (
        <div className={`${className ? className : ""} relative items-center flex justify-center`}>
            <button className="videoPlayerControls" onClick={() => setShowList(!showList)}>
                <QueueListIcon className={"h-full w-full"}/>
            </button>
            {showList && (
                <ul className="fixed bottom-[7.5%] right-2 w-[45%] rounded-md shadow-md bg-[#262626] max-h-[65vh] overflow-hidden overflow-y-scroll scrollbar-hidden"
                    onMouseLeave={() => setShowList(false)}>
                    {episodes.map((episode: Episode, index) => (
                        <li key={index}
                            className={`z-50 px-4 flex flex-col w-full h-auto items-center
                            ${selectedEpisode?.number === episode.number ? "bg-[#131313] py-8" : "hover:bg-[#3c3c3c]"}`}
                            onClick={() => handleClickedEpisode(episode.number)}>
                            <div className={"flex flex-row justify-start space-x-5 w-full py-4"}>
                                <a className="block text-lg text-white font-poppins">{episode.number}</a>
                                <a className="block text-lg text-white font-poppins">{episode.title}</a>
                            </div>
                            <div
                                className={`${selectedEpisode?.number === episode.number ? "items-center flex flex-row justify-between space-x-5 w-full" : "hidden"}`}>
                                <Image src={episode.image} width={2000} height={2000} alt={episode.title}
                                       className={"rounded-md w-[35%]"}/>
                                <a className={"block text-lg text-white font-poppins text-start"}>{episode.description}</a>
                            </div>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    )
}
