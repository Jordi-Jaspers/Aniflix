import {infoScreenState} from "@atoms/InfoScreenAtom";
import {showSearchResultsState} from "@atoms/SearchResultScreen";
import Header from "@components/header/Header";
import HomeScreen from "@components/HomeScreen";
import InfoScreen from "@components/infoScreen/InfoScreen";
import SearchResultScreen from "@components/header/search/SearchResultScreen";
import AnimeService from "@consumet/AnimeService";
import {Anime} from "@interfaces/Anime";
import Head from 'next/head'
import React from "react";
import {useRecoilValue} from "recoil";

interface Props {
    anime: Anime
}

export default function Home({anime}: Props) {
    const showInfoScreen = useRecoilValue(infoScreenState)
    const showSearchResults = useRecoilValue(showSearchResultsState)
    
    return (
        <div className={`relative h-[100%] bg-[#141414] z-0 ${showInfoScreen && '!h-screen overflow-hidden'}`}>
            {/*  TAB TITLE  */}
            <Head>
                <title> Home | Aniflix</title>
                <link rel="icon" href="/favicon.ico"/>
            </Head>
            
            {/*  HEADER  */}
            <Header/>
            
            <main className="lg:space-t-24 h-fit">
                {showSearchResults
                    ? <SearchResultScreen/>
                    : <HomeScreen anime={anime}/>
                }
            </main>
            
            {/*  ANIME INFO SCREEN */}
            {showInfoScreen && <InfoScreen/>}
            
            {/* FOOTER */}
            <footer className={"flex flex-row items-center justify-center p-8 w-full"}>
                <a className={"font-poppins text-[#d1d1d1]"} href={"https://github.com/Jordi-Jaspers"}>
                    © Designed and built by Jordi Jaspers
                </a>
            </footer>
        </div>
    )
}

export async function getServerSideProps() {
    const animes: Anime[] = await AnimeService.getPopularAnime(50, 1)
    const anime: Anime = animes[Math.floor(Math.random() * animes.length)]
    console.log(anime)
    return {
        props: {
            anime: anime
        }
    }
}
