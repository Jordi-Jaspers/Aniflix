import Header from "@components/header/Header";
import ResultCard from "@components/ResultCard";
import AnimeService from "@consumet/AnimeService";
import {useDynamicColumns} from "@hooks/useDynamicColumns";
import {Anime} from "@interfaces/Anime";
import Head from "next/head";
import React, {useEffect, useState} from "react";

export default function Trending() {
    const cols = useDynamicColumns(205)
    const [currentPage, setCurrentPage] = useState(1);
    const [anime, setAnime] = useState<Anime[]>([]);
    
    useEffect(() => {
        AnimeService.getTrendingAnime(50, currentPage + 1).then((fetchedAnime) => {
            setAnime([...anime, ...fetchedAnime]);
        })
    }, [currentPage]);
    
    return (
        <div className={"relative h-full w-full bg-[#1E1E25] z-0"}>
            {/*  TAB TITLE  */}
            <Head>
                <title>Aniflix | Trending</title>
                <link rel="icon" href="/favicon.ico"/>
            </Head>
            
            {/*  HEADER  */}
            <Header/>
            
            <section className={"pl-4 pr-4 md:pl-6 md:pr-6 lg:pl-12 flex flex-col"}>
                <div className="h-fit pt-[2.5%] space-y-4">
                    <h1 className="font-poppins font-semibold text-[#e5e5e5] text-2xl">
                        Popular Anime
                    </h1>
                </div>
                <div className={"min-h-screen h-fit z-10 flex justify-evenly"}>
                    <div className={`grid gap-4 py-4`}
                         style={{gridTemplateColumns: `repeat(${cols}, minmax(0, 1fr))`}}>
                        {anime.map((result) => (
                            <ResultCard
                                id={result.id.toString()}
                                title={result.title.english ? result.title.english : result.title.romaji}
                                image={result.image}
                                rating={result.rating}
                                status={result.status}
                                totalEpisodes={result.totalEpisodes}
                            />
                        ))}
                        <div className="min-w-[220px] max-w-[250px] h-full overflow-hidden">
                            <div
                                className={"rounded-md cursor-pointer overflow-hidden bg-[#1a1920] flex flex-col h-full justify-center items-center"}
                                onClick={() => setCurrentPage(currentPage + 1)}>
                                <div className={"rounded-full bg-[#1E1E25]/80 aspect-square w-48 flex items-center justify-center"}>
                                    <p className={"font-poppins text-[#666666] hover:text-white"}> Show more...</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    )
}
