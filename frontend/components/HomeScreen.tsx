
import Banner from "@components/Banner";
import Row from "@components/Row";
import AnimeService from "@consumet/AnimeService";
import {Genres, getRandomGenres} from "@enum/Genre";
import {useFetchAnime} from "@hooks/useFetchAnime";
import {Anime} from "@interfaces/Anime";
import {RecentEpisode} from "@interfaces/RecentEpisode";

import React from "react";

interface Props {
    className?: string
    request: () => Promise<Anime[]> | Promise<RecentEpisode[]>
}

export default function HomeScreen({className, request}: Props) {
    const randomGenres = getRandomGenres(6)
    const {anime, loading} = useFetchAnime(request)
    
    return (
        <div className={className}>
            {/*  RANDOM ANIME BANNER */}
    
            {anime && <Banner anime={anime as Anime}/>}
            
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
        </div>
    )
}
