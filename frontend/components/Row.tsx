import {ChevronLeftIcon, ChevronRightIcon} from '@heroicons/react/24/solid'
import {useEffect, useRef, useState} from 'react'
import {Anime} from "../interfaces/Anime";
import {GoEpisode} from "../interfaces/GoEpisode";
import Thumbnail from "./icons/Thumbnail";
import {Page} from "../interfaces/Page";

interface Props {
    title: string
    page: Page<Anime> | Page<GoEpisode>
}

export default function Row({title, page}: Props) {
    const rowRef = useRef<HTMLDivElement>(null)
    const animes: Anime[] | GoEpisode[] = page.results

    const handleClick = (direction: string) => {
        if (rowRef.current) {
            const {scrollLeft, clientWidth} = rowRef.current
            const scrollTo =
                direction === 'left'
                    ? scrollLeft - clientWidth
                    : scrollLeft + clientWidth
            rowRef.current.scrollTo({left: scrollTo, behavior: 'smooth'})
        }
    }

    return (
        <div className="h-fit pt-[2.5%] space-y-0.5 md:space-y-2 pl-4 pr-4 md:pl-6 md:pr-6 lg:pl-12 ">
            <h2 className="font-poppins w-56 cursor-pointer text-sm font-semibold text-[#e5e5e5] transition duration-200 hover:text-white md:text-2xl">
                {title}
            </h2>
            <div className="group relative md:-ml-2">
                <ChevronLeftIcon className={`arrowIcon left-2`} onClick={() => handleClick('left')}/>
                <div className="flex items-center space-x-0.5 overflow-x-scroll scrollbar-hide md:space-x-2.5 md:p-2" ref={rowRef}>
                    {animes?.map((anime) => (
                        <Thumbnail key={anime.id} anime={anime}/>
                    ))}
                </div>
                <ChevronRightIcon
                    className="arrowIcon right-2"
                    onClick={() => handleClick('right')}
                />
            </div>
        </div>
    )
}
