import {ChevronLeftIcon, ChevronRightIcon} from '@heroicons/react/24/outline'
import {useRef, useState} from 'react'
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
    const [isMoved, setIsMoved] = useState(false)

    console.log('page', page)
    const animes: Anime[] | GoEpisode[] = page.results
    console.log('animes', animes)

    const handleClick = (direction: string) => {
        setIsMoved(true)
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
        <div className="h-fit pt-[2.5%] space-y-0.5 md:space-y-2">
            <h2 className="font-poppins w-56 cursor-pointer text-sm font-semibold text-[#e5e5e5] transition duration-200 hover:text-white md:text-2xl">
                {title}
            </h2>
            <div className="group relative md:-ml-2">
                <ChevronLeftIcon
                    className={`absolute top-0 bottom-0 left-2 z-40 m-auto h-9 w-9 cursor-pointer opacity-0 transition hover:scale-125 group-hover:opacity-100 ${
                        !isMoved && 'hidden'
                    }`}
                    onClick={() => handleClick('left')}
                />
                <div className="flex items-center space-x-0.5 overflow-x-scroll scrollbar-hide md:space-x-2.5 md:p-2" ref={rowRef}>
                    {animes?.map((anime) => (
                        <Thumbnail key={anime.id} anime={anime}/>
                    ))}
                </div>
                <ChevronRightIcon
                    className="absolute top-0 bottom-0 right-2 z-40 m-auto h-9 w-9 cursor-pointer opacity-0 transition hover:scale-125 group-hover:opacity-100"
                    onClick={() => handleClick('right')}
                />
            </div>
        </div>
    )
}
