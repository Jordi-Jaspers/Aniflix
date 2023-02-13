

import {infoScreenState} from "@atoms/InfoScreenAtom";
import {showSearchResultsState} from "@atoms/SearchResultScreen";
import Footer from "@components/footer/Footer";
import Header from "@components/header/Header";
import SearchResultScreen from "@components/header/search/SearchResultScreen";
import HomeScreen from "@components/HomeScreen";
import InfoScreen from "@components/infoScreen/InfoScreen";
import AnimeService from "@consumet/AnimeService";
import {Anime} from "@interfaces/Anime";

import React from "react";

import Head from 'next/head'
import {useRecoilValue} from "recoil";


interface Props {
    anime: Anime
}

export default function Home({anime}: Props) {
    const showInfoScreen = useRecoilValue(infoScreenState)
    const showSearchResults = useRecoilValue(showSearchResultsState)
    
    return (
        <div className={`relative h-[100%] bg-[#1a1920] z-0 ${showInfoScreen && '!h-screen overflow-hidden'}`}>
            {/*  TAB TITLE  */}
            <Head>
                <title>Aniflix</title>
                <link rel="icon" href="/favicon.ico"/>
            </Head>
            
            {/*  HEADER  */}
            <Header/>
            
            {/*  MAIN  */}
            <main className="lg:space-t-24 h-fit">
                <HomeScreen request={AnimeService.getTrendingAnime} className={showSearchResults ? "hidden" : "visible"}/>
                {showSearchResults && <SearchResultScreen/>}
                {showInfoScreen && <InfoScreen/>}
            </main>
    
            {/*  FOOTER  */}
            <Footer/>
        </div>
    )
}
