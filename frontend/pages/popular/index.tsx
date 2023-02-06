import Header from "@components/header/Header";
import ResultCard from "@components/ResultCard";
import AnimeService from "@consumet/AnimeService";
import {useDynamicColumns} from "@hooks/useDynamicColumns";
import {Anime} from "@interfaces/Anime";
import Head from "next/head";
import React, {useEffect, useState} from "react";

export default function Popular() {
    const cols = useDynamicColumns(275)
    const [currentPage, setCurrentPage] = useState(1);
    const [anime, setAnime] = useState<Anime[]>([]);
    
    useEffect(() => {
        AnimeService.getPopularAnime(50, currentPage + 1).then((fetchedAnime) => {
            setAnime([...anime, ...fetchedAnime]);
        })
    }, [currentPage]);
    
    return (
        <div className={"relative h-full w-full bg-[#1E1E25] z-0"}>
            {/*  TAB TITLE  */}
            <Head>
                <title>Aniflix | Popular</title>
                <link rel="icon" href="/favicon.ico"/>
            </Head>
            
            {/*  HEADER  */}
            <Header/>
            
            <section className={"min-h-screen h-fit z-10 bg-[#1E1E25] flex justify-evenly"}>
                <div className={`grid gap-4 py-4`}
                     style={{gridTemplateColumns: `repeat(${cols}, minmax(0, 1fr))`}}>
                    {anime.map((result) => (
                        <ResultCard anime={result}/>
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
            </section>
        </div>
    )
}
