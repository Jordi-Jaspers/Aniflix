import Header from "@components/header/Header";
import AnimeService from "@consumet/AnimeService";
import {useDynamicColumns} from "@hooks/useDynamicColumns";
import {Anime} from "@interfaces/Anime";
import LibraryService from "@service/LibraryService";
import {LOGGER} from "@util/Logger";
import Head from "next/head";
import {ListResult} from "pocketbase";
import React, {useEffect, useState} from "react";
interface Props {
    records: Record<any, any>[];
}
export default function Library() {
    const [records, setRecords] = useState<Record<any, any>[]>([]);
    
    useEffect(() => {
        LibraryService.getLibrary().then((fetchedRecords) => {
            setRecords(fetchedRecords);
        })
    }, []);
    
    return (
        <>
            <div className={"relative h-full w-full bg-[#1E1E25] z-0"}>
                {/*  TAB TITLE  */}
                <Head>
                    <title>Aniflix | Library</title>
                    <link rel="icon" href="/favicon.ico"/>
                </Head>
        
                {/*  HEADER  */}
                <Header/>
                
                <section className={"flex flex-col"}>
                    {records.map((record) => (
                        <span className={"font-poppins text-white text-xl"}>
                            {record.title}
                        </span>
                    ))}
                </section>
            </div>
        </>
    )
}
