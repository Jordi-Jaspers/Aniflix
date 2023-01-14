import {ChevronLeftIcon, ChevronRightIcon} from '@heroicons/react/24/solid'
import Thumbnail from "@icons/Thumbnail";
import {Anime} from "@interfaces/Anime";
import {Page} from "@interfaces/Page";
import {useRef} from 'react'

interface Props {
    title: string
    page: Page<Anime>
}

export default function Row({title, page}: Props) {
    const rowRef = useRef<HTMLDivElement>(null)
    const animes: Anime[] = page.results

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
        <div className="h-fit pt-[2.5%] space-y-0.5 md:space-y-2">
            <h2 className="pl-4 pr-4 md:pl-6 md:pr-6 lg:pl-12 font-poppins w-fit cursor-pointer text-sm font-semibold text-[#e5e5e5] transition duration-200 hover:text-white md:text-2xl">
                {title}
            </h2>
            <div className="group relative md:-ml-2">
                <div className={"arrowIcon left-0 bg-gradient-to-r"}>
                    <ChevronLeftIcon className={"opacity-0 transition hover:scale-125 group-hover:opacity-100"}
                                     onClick={() => handleClick('left')}/>
                </div>
                <div className="flex items-center space-x-0.5 overflow-x-scroll scrollbar-hide md:space-x-2.5 px-4 md:px-6 lg:px-12"
                     ref={rowRef}>
                    {animes?.map((anime) => (
                        <Thumbnail key={anime.id} anime={anime}/>
                    ))}
                </div>
                <div className={"arrowIcon right-0 bg-gradient-to-l"}>
                    <ChevronRightIcon className={"opacity-0 transition hover:scale-125 group-hover:opacity-100"}
                                      onClick={() => handleClick('right')}/>
                </div>
            </div>
        </div>
    )
}
