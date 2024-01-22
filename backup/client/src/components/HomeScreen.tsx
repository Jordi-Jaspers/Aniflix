import Banner from "@components/Banner";
import Row from "@components/Row";
import AnimeService from "@consumet/AnimeService";
import type {Genres} from "@enum/Genre";
import {getRandomGenres} from "@enum/Genre";

interface Props {
    className?: string;
}

export default function HomeScreen({className}: Props) {
    const randomGenres = getRandomGenres(6);

    return (
        <div className={className}>
            {/*  RANDOM ANIME BANNER */}
            <Banner request={AnimeService.getTrendingAnime}/>

            <section className={"z-10 h-fit min-h-screen"}>
                {/*  RECENTLY ADDED ANIME CAROUSEL */}
                <Row
                    title="Recently Added"
                    request={AnimeService.getRecentEpisodes}
                    priority={true}
                />

                {/*/!*  TRENDING ANIME CAROUSEL *!/*/}
                <Row
                    title="Trending"
                    request={AnimeService.getTrendingAnime}
                    priority={true}
                />

                {/*/!*  POPULAR ANIME CAROUSEL *!/*/}
                <Row
                    title="Popular"
                    request={AnimeService.getPopularAnime}
                    priority={true}
                />

                {/*/!*  ANIME CAROUSEL BY GENRE *!/*/}
                {randomGenres.map((genre: Genres, key: number) => (
                    <Row
                        key={key}
                        title={genre}
                        request={AnimeService.getAnimeByGenre}
                        genre={genre}
                    />
                ))}
            </section>
        </div>
    );
}
