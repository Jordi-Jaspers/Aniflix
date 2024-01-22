import {infoScreenState} from "@atoms/InfoScreenAtom";
import {showSearchResultsState} from "@atoms/SearchResultScreen";
import Footer from "@components/footer/Footer";
import Header from "@components/header/Header";
import SearchResultScreen from "@components/header/search/SearchResultScreen";
import InfoScreen from "@components/infoScreen/InfoScreen";
import ResultCard from "@components/ResultCard";
import AnimeService from "@consumet/AnimeService";
import {useDynamicColumns} from "@hooks/useDynamicColumns";
import type {Anime} from "@interfaces/Anime";
import {LOGGER} from "@util/Logger";

import React, {useEffect, useState} from "react";

import Head from "next/head";
import {useRecoilValue} from "recoil";


export default function Trending() {
    const {cols, width} = useDynamicColumns()
    const showInfoScreen = useRecoilValue(infoScreenState)
    const showSearchResults = useRecoilValue(showSearchResultsState)
    const [currentPage, setCurrentPage] = useState(1);
    const [anime, setAnime] = useState<Anime[]>([]);

    useEffect(() => {
        if (anime.length / 50 < currentPage) {
            AnimeService.getTrendingAnime(50, currentPage + 1).then((fetchedAnime) => {
                setAnime([...anime, ...fetchedAnime]);
            }).catch((e) => {
                LOGGER.error("Failed to fetch trending anime", e);
            });
        }
    }, [currentPage, anime]);

    return (
        <div className={"relative h-full w-full bg-[#1E1E25] z-0"}>
            {/*  TAB TITLE  */}
            <Head>
                <title>Aniflix | Trending</title>
                <link rel="icon" href="/favicon.ico"/>
            </Head>

            {/*  HEADER  */}
            <Header/>

            {showSearchResults && <SearchResultScreen/>}
            {showInfoScreen && <InfoScreen/>}

            {!showSearchResults &&
                <section className={"pl-4 pr-4 md:pl-6 md:pr-6 lg:pl-12 flex flex-col"}>
                    <div className="h-fit pt-[2.5%] space-y-4">
                        <h1 className="font-poppins font-semibold text-[#e5e5e5] text-4xl">
                            Trending Anime
                        </h1>
                    </div>
                    <div className={"flex flex-col justify-center items-center"}>
                        <div className={"min-h-screen h-fit z-10 flex justify-evenly"}>
                            <div className={`grid gap-4 py-4`}
                                 style={{gridTemplateColumns: `repeat(${cols}, minmax(0, 1fr))`}}>
                                {anime.map((result) => (
                                    <ResultCard
                                        key={result.id}
                                        id={result.id.toString()}
                                        title={result.title.english ? result.title.english : result.title.romaji}
                                        image={result.image}
                                        rating={result.rating}
                                        status={result.status}
                                        totalEpisodes={result.totalEpisodes}
                                        width={width}
                                    />
                                ))}
                            </div>
                        </div>
                        <button
                            className="btn btn-sm border-gray-500 hover:border-white bg-[#1a1920] text-gray-500 hover:text-[#e5e5e5] w-fit m-6"
                            onClick={() => setCurrentPage(currentPage + 1)}>
                            <span className={"font-poppins text-md "}>Load More</span>
                        </button>
                    </div>
                </section>
            }
            {/*  FOOTER  */}
            <Footer/>
        </div>
    )
}
