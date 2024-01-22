import {infoScreenState} from "@atoms/InfoScreenAtom";
import {showSearchResultsState} from "@atoms/SearchResultScreen";
import Footer from "@components/footer/Footer";
import Header from "@components/header/Header";
import SearchResultScreen from "@components/header/search/SearchResultScreen";
import InfoScreen from "@components/infoScreen/InfoScreen";
import ResultCard from "@components/ResultCard";
import {WATCH_STATUS_LIST} from "@enum/WatchStatus";
import {useDynamicColumns} from "@hooks/useDynamicColumns";
import LibraryService from "@service/LibraryService";
import {LOGGER} from "@util/Logger";

import React, {useEffect, useState} from "react";

import {ChevronUpIcon} from "@heroicons/react/24/solid";
import Head from "next/head";
import {useRecoilValue} from "recoil";

export default function Library() {
    const {cols, width} = useDynamicColumns()
    const [records, setRecords] = useState<Record<any, any>[]>([]);
    const [watchStatuses, setWatchStatuses] = useState<string[]>([]);
    const [showFilters, setShowFilters] = useState<boolean>(false);
    const showInfoScreen = useRecoilValue(infoScreenState)
    const showSearchResults = useRecoilValue(showSearchResultsState)

    const getUniformTitle = (title: string) => {
        return title.toLowerCase().replace("_", " ").replace(/\b[a-z]/g, (letter) => letter.toUpperCase())
    }

    useEffect(() => {
        LibraryService.getLibrary().then((fetchedRecords) => {
            setRecords(fetchedRecords);
            setWatchStatuses(WATCH_STATUS_LIST);
        }).catch((e) => {
            LOGGER.error("Failed to fetch library", e);
        });
    }, []);

    return (
        <main className={"relative h-full w-full bg-[#1E1E25] z-0"}>
            {/*  TAB TITLE  */}
            <Head>
                <title>Aniflix | Library</title>
                <link rel="icon" href="/favicon.ico"/>
            </Head>

            {/*  HEADER  */}
            <Header/>

            {showSearchResults && <SearchResultScreen/>}
            {showInfoScreen && <InfoScreen/>}

            {!showSearchResults &&
                <section className={"pl-4 pr-4 md:pl-6 md:pr-6 lg:pl-12 flex flex-col"}>
                    <div className="h-fit pt-[2.5%] flex flex-row items-end space-x-2">
                        <h1 className="font-poppins font-semibold text-[#e5e5e5] text-4xl">
                            My Library
                        </h1>
                        <button className="btn btn-sm border-white hover:border-white bg-[#1a1920] space-x-2"
                                onClick={() => setShowFilters(!showFilters)}>
                            <span className={"font-poppins text-md text-[#e5e5e5]"}>Filters</span>
                            <ChevronUpIcon className={`${showFilters ? `rotate-180` : ""} w-4 h-4 duration-300`}/>
                        </button>
                    </div>

                    <div className={`${
                        showFilters
                            ? "ease-in duration-500 h-fit w-full transition-all flex items-center space-x-0.5 overflow-x-scroll scrollbar-hide justify-center"
                            : "ease-out duration-500 h-0 w-full transition-all flex items-center space-x-0.5 overflow-x-scroll scrollbar-hide justify-center"}`}>
                        {watchStatuses.map((status) => (
                            <div key={status}
                                 className={"bg-[#1a1920]/80 px-4 py-2 rounded-full text-[#d2d2d2] hover:text-white cursor-pointer"}>
                            <span className={"font-poppins text-sm"}>
                                {getUniformTitle(status)}
                            </span>
                            </div>
                        ))}
                    </div>

                    <div className={"min-h-screen h-fit z-10 flex justify-evenly"}>
                        <div className={`grid gap-4 py-4 h-fit`}
                             style={{gridTemplateColumns: `repeat(${cols}, minmax(0, 1fr))`}}>
                            {records.map((record) => (
                                <ResultCard
                                    key={record.anime_id as string}
                                    id={record.anime_id as string}
                                    title={record.title as string}
                                    image={record.image as string}
                                    rating={record.rating as number}
                                    status={record.status as string}
                                    totalEpisodes={record.total_episodes as number}
                                    width={width}
                                />
                            ))}
                        </div>
                    </div>
                </section>
            }
            {/*  FOOTER  */}
            <Footer/>
        </main>
    )
}
