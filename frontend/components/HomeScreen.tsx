import Banner from "@components/Banner";
import Row from "@components/Row";
import AnimeService from "@consumet/AnimeService";
import {Genres, getRandomGenres} from "@enum/Genre";
import {Anime} from "@interfaces/Anime";
import React from "react";

interface Props {
    anime: Anime
}

export default function HomeScreen({anime}: Props) {
    const randomGenres = getRandomGenres(4)
    return (
        <>
            {/*  RANDOM ANIME BANNER */}
            <Banner anime={anime}/>
            
            <section className={"min-h-screen h-fit z-10"}>
                {/*  RECENTLY ADDED ANIME CAROUSEL */}
                <Row title="Recently Added" request={AnimeService.getRecentEpisodes} priority={true}/>
                
                {/*/!*  TRENDING ANIME CAROUSEL *!/*/}
                <Row title="Trending" request={AnimeService.getTrendingAnime} priority={true}/>
                
                {/*/!*  POPULAR ANIME CAROUSEL *!/*/}
                <Row title="Popular" request={AnimeService.getPopularAnime} priority={true}/>
                
                {/*/!*  ANIME CAROUSEL BY GENRE *!/*/}
                {randomGenres.map((genre: Genres, key: number) => (
                    <Row key={key} title={genre} request={AnimeService.getAnimeByGenre} genre={genre}/>
                ))}
            </section>
        </>
    )
}
