import Head from 'next/head'
import Header from "../components/Header";
import {Anime} from "../interfaces/Anime";
import ConsumetApi from "../utils/ConsumetApi";
import {Genres, getRandomGenres} from "../enum/Genre";
import Banner from "../components/Banner";
import {Page} from "../interfaces/Page";
import Row from "../components/Row";
import {useRecoilValue} from "recoil";
import {animeState} from "../atoms/AnimeAtom";
import InfoScreen from "../components/InfoScreen";

interface Props {
    randomAnime: Anime;
    recentEpisodes: Page<Anime>;
    trendingAnimes: Page<Anime>;
    popularAnimes: Page<Anime>;
    genre1Animes: Page<Anime>;
    genre2Animes: Page<Anime>;
    genre3Animes: Page<Anime>;
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
                                 randomGenres
                             }: Props) {
    const anime = useRecoilValue(animeState)
    return (
        <div className={"bg-[#141414] -z-100"}>
            <div className={"h-screen relative bg-gradient overflow-scroll lg:h-[140vh] z-0"}>
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
                        <div className={"bg-gradient-to-b from-transparent via-black to-[#141414] "}>
                            <div className={"pt-[7.5%]"}>
                                {/*  RECENTLY ADDED ANIME CAROUSEL */}
                                <Row title="Recently Added" page={recentEpisodes}/>
                            </div>
                        </div>

                        <div className={"h-fit"}>
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
                        </div>
                    </section>
                </main>

                {/*  ANIME INFO SCREEN */}
                {anime && <InfoScreen/>}

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
    const randomGenres = getRandomGenres(4)
    const [
        recentEpisodes,
        trendingAnimes,
        popularAnimes,
        genre1Animes,
        genre2Animes,
        genre3Animes,
        genre4Animes
    ] = await Promise.all([
        fetch(ConsumetApi.fetchRecentEpisodes).then((res) => res.json()),
        fetch(ConsumetApi.fetchTrending).then((res) => res.json()),
        fetch(ConsumetApi.fetchPopularAnime).then((res) => res.json()),
        fetch(ConsumetApi.fetchGenre.replace("{genre}", randomGenres[0])).then((res) => res.json()),
        fetch(ConsumetApi.fetchGenre.replace("{genre}", randomGenres[1])).then((res) => res.json()),
        fetch(ConsumetApi.fetchGenre.replace("{genre}", randomGenres[2])).then((res) => res.json()),
        fetch(ConsumetApi.fetchGenre.replace("{genre}", randomGenres[3])).then((res) => res.json()),
    ])

    let randomAnime: Anime;
    let animeList: Anime[] = [...popularAnimes.results, ...trendingAnimes.results, ...genre1Animes.results, ...genre2Animes.results, ...genre3Animes.results, ...genre4Animes.results]
    do {
        randomAnime = animeList[Math.floor(Math.random() * animeList.length)];
    } while (!randomAnime.cover || !randomAnime.title)

    if (!randomAnime) {
        randomAnime = await fetch(ConsumetApi.fetchAnimeDetails.replace("{id}", "16498")).then((res) => res.json());
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
            randomGenres
        }
    }
}
