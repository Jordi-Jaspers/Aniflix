import {infoScreenState} from "@atoms/InfoScreenAtom";
import {showSearchResultsState} from "@atoms/SearchResultScreen";
import Header from "@components/header/Header";
import SearchResultScreen from "@components/header/search/SearchResultScreen";
import InfoScreen from "@components/infoScreen/InfoScreen";
import ResultCard from "@components/ResultCard";
import {WATCH_STATUS_LIST} from "@enum/WatchStatus";
import {useDynamicColumns} from "@hooks/useDynamicColumns";
import LibraryService from "@service/LibraryService";
import Head from "next/head";
import React, {useEffect, useState} from "react";
import {useRecoilValue} from "recoil";

export default function Library() {
    const cols = useDynamicColumns(205);
    const [records, setRecords] = useState<Record<any, any>[]>([]);
    const [watchStatuses, setWatchStatuses] = useState<string[]>([]);
    const showInfoScreen = useRecoilValue(infoScreenState)
    const showSearchResults = useRecoilValue(showSearchResultsState)
    
    const getUniformTitle = (title: string) => {
        return title.toLowerCase().replace("_", " ").replace(/\b[a-z]/g, (letter) => letter.toUpperCase())
    }
    
    useEffect(() => {
        LibraryService.getLibrary().then((fetchedRecords) => {
            setRecords(fetchedRecords);
            setWatchStatuses(WATCH_STATUS_LIST);
        })
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
                    <div className="h-fit pt-[2.5%] space-y-4">
                        <h1 className="font-poppins font-semibold text-[#e5e5e5] text-2xl">
                            My Library
                        </h1>
                        <div className="flex items-center space-x-0.5 overflow-x-scroll scrollbar-hide w-full justify-center">
                            {watchStatuses.map((status) => (
                                <div className={"bg-[#1a1920]/80 px-4 py-2 rounded-full text-[#d2d2d2] hover:text-white cursor-pointer"}>
                                <span className={"font-poppins text-sm"}>
                                    {getUniformTitle(status)}
                                </span>
                                </div>
                            ))}
                        </div>
                    </div>

                    <div className={"min-h-screen h-fit z-10 flex justify-evenly"}>
                        <div className={`grid gap-4 py-4 h-fit`} style={{gridTemplateColumns: `repeat(${cols}, minmax(0, 1fr))`}}>
                            {records.map((record) => (
                                <ResultCard
                                    id={record.anime_id}
                                    title={record.title}
                                    image={record.image}
                                    rating={record.rating}
                                    status={record.status}
                                    totalEpisodes={record.total_episodes}
                                />
                            ))}
                        </div>
                    </div>
                </section>
            }
        </main>
    )
}
