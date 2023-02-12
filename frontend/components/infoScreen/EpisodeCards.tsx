import EpisodeCard from "@components/infoScreen/EpisodeCard";
import {Episode} from "@interfaces/Episode";

import React, {useState} from "react";

import {ChevronDownIcon} from "@heroicons/react/24/solid";



interface Props {
    className?: string;
    episodes: Episode[]
}

export default function EpisodeCards({className, episodes}: Props) {
    const [showList, setShowList] = useState(false);
    const [page, setPage] = useState(1);
    
    const totalPages = Math.ceil(episodes.length / 25);
    const lowerBound = (page: number) => {
        return page === 1 ? 1 : ((page - 1) * 25) + 1;
    }
    const upperBound = (page: number) => {
        return page * 25;
    }
    
    let list = [];
    for (let i = 1; i <= totalPages; i++) {
        list.push(
            <li key={i} className={"px-4 flex justify-start space-x-8 w-full h-auto items-start hover:bg-[#1a1920] rounded"}
                onClick={() => setPage(i)}>
                <a className={"block text-white font-poppins min-w-[75px] w-[30%]"}>Page {i}</a>
                <a className={"block text-white font-poppins font-light text-sm min-w-fit w-[70%]"}>(Episodes {lowerBound(i)} - {upperBound(i)})</a>
            </li>
        );
    }
    
    return (
        <div className={className ? className : ""}>
            <div className="relative flex flex-row justify-between rounded-b-md py-4">
                <h4 className={"infoScreenTitle py-0"}>Episodes</h4>
                <button
                    className={"text-lg text-white items-center flex flex-row justify-between w-fit py-1.5 pl-4 pr-6 bg-[#1E1E25] border-[#4d4d4d] border rounded text-center space-x-2"}
                    onClick={() => setShowList(!showList)}>
                    <ChevronDownIcon className={"h-6 w-6"}/>
                    <p>Total: {episodes?.length}</p>
                </button>
                {showList && (
                    <ul className={"absolute z-40 right-0 top-[4em] p-2 min-w-[15%] w-fit rounded-md shadow-md bg-[#1E1E25] max-h-[65vh] overflow-hidden overflow-y-scroll scrollbar-hidden border-[#4d4d4d] border"}
                        onClick={() => setShowList(!showList)}
                        onMouseLeave={() => setShowList(!showList)}>
                        {list}
                    </ul>
                )}
            </div>
            {episodes?.map((episode) => {
                if (episode.number >= lowerBound(page) && episode.number <= upperBound(page)) {
                    if (episode.description && episode.title) {
                        return (<EpisodeCard key={episode.number} episode={episode}/>)
                    }
                }
            })}
        </div>
    )
}
