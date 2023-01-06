import Head from 'next/head'
import Header from "../components/Header";
import {Anime} from "../interfaces/Anime";
import {GoEpisode} from "../interfaces/GoEpisode";
import requests from "../utils/requests";
import {Genres, getRandomGenres} from "../enum/Genre";
import Banner from "../components/Banner";
import {Page} from "../interfaces/Page";
import Row from "../components/Row";
import {useEffect, useState} from 'react'

interface Props {
    randomAnime: Anime;
    recentEpisodes: Page<GoEpisode>;
    trendingAnimes: Page<Anime>;
    popularAnimes: Page<Anime>;
    genre1Animes: Page<Anime>;
    genre2Animes: Page<Anime>;
    genre3Animes: Page<Anime>;
    genre4Animes: Page<Anime>;
    randomGenres: Genres[];
}

export default function Home({
     randomAnime,
     recentEpisodes,
     trendingAnimes,
     popularAnimes,
     genre1Animes,
     genre2Animes,
     genre3Animes,
     genre4Animes,
    randomGenres
 }: Props) {
    return (
        <div className={"bg-[#141414] -z-100"}>
            <div className={"h-screen relative bg-gradient-to-t from-[#141414] to-transparent overflow-scroll lg:h-[140vh] z-0"}>
                {/*  TAB TITLE  */}
                <Head>
                    <title>Home | Aniflix</title>
                </Head>

                {/*  HEADER  */}
                <Header/>

                <main className="lg:space-t-24 h-fit">
                    {/*  RANDOM ANIME BANNER */}
                    <Banner randomAnime={randomAnime}/>

                    <section>
                        <div className={"bg-gradient-to-b from-transparent via-black to-[#141414]"}>
                            <div className={"xs:pt-[5%] sm:pt-[7.5%] lg:pt-[10%] 2xl:pt-[15%] pl-4 pr-4 md:pl-6 md:pr-6 lg:pl-12"}>
                                {/*  RECENTLY ADDED ANIME CAROUSEL */}
                                <Row title="Recently Added" page={recentEpisodes}/>
                            </div>
                        </div>

                        <div className={"pl-4 pr-4 md:pl-6 md:pr-6 lg:pl-12 h-fit"}>
                            {/*  TRENDING ANIME CAROUSEL */}
                            <Row title="Trending" page={trendingAnimes}/>

                            {/*  POPULAR ANIME CAROUSEL */}
                            <Row title="Popular" page={popularAnimes}/>

                            {/*  GENRE 1 ANIME CAROUSEL */}
                            <Row title={randomGenres[0]} page={genre1Animes}/>

                            {/*  GENRE 2 ANIME CAROUSEL */}
                            <Row title={randomGenres[1]} page={genre2Animes}/>

                            {/*  GENRE 3 ANIME CAROUSEL */}
                            <Row title={randomGenres[2]} page={genre3Animes}/>

                            {/*  GENRE 4 ANIME CAROUSEL */}
                            <Row title={randomGenres[3]} page={genre4Animes}/>
                        </div>
                    </section>
                </main>

                {/* FOOTER */}
                <footer className={"flex flex-row items-center justify-center p-8 w-full"}>
                    <a className={"font-poppins text-[#d1d1d1]"} href={"https://github.com/Jordi-Jaspers"}>
                        © Designed and built by Jordi Jaspers
                    </a>
                </footer>
            </div>
        </div>
    )
}

export async function getServerSideProps() {
    const randomGenres = getRandomGenres();
    const [
        recentEpisodes,
        trendingAnimes,
        popularAnimes,
        genre1Animes,
        genre2Animes,
        genre3Animes,
        genre4Animes
    ] = await Promise.all([
        fetch(requests.fetchRecentEpisodes).then((res) => res.json()),
        fetch(requests.fetchTrending).then((res) => res.json()),
        fetch(requests.fetchPopularAnime).then((res) => res.json()),
        fetch(requests.fetchGenre.replace("{genre}", randomGenres[0])).then((res) => res.json()),
        fetch(requests.fetchGenre.replace("{genre}", randomGenres[1])).then((res) => res.json()),
        fetch(requests.fetchGenre.replace("{genre}", randomGenres[2])).then((res) => res.json()),
        fetch(requests.fetchGenre.replace("{genre}", randomGenres[3])).then((res) => res.json()),
    ])

    let randomAnime: Anime;
    let animeList: Anime[] = [...popularAnimes.results, ...trendingAnimes.results];
    do {
        randomAnime = animeList[Math.floor(Math.random() * animeList.length)];
    } while (!randomAnime.cover || !randomAnime.title)

    if (!randomAnime) {
        randomAnime = await fetch(requests.fetchAnimeDetails.replace("{id}", "16498")).then((res) => res.json());
    }

    return {
        props: {
            randomAnime: randomAnime,
            recentEpisodes: recentEpisodes,
            trendingAnimes: trendingAnimes,
            popularAnimes: popularAnimes,
            genre1Animes: genre1Animes,
            genre2Animes: genre2Animes,
            genre3Animes: genre3Animes,
            genre4Animes: genre4Animes,
            randomGenres
        }
    }
}
