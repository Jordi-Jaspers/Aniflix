import {animeState} from "@atoms/AnimeAtom";
import Banner from "@components/Banner";
import Header from "@components/Header";
import InfoScreen from "@components/InfoScreen";
import Row from "@components/Row";
import {Genres, getRandomGenres} from "@enum/Genre";
import {Anime} from "@interfaces/Anime";
import {RecentEpisode} from "@interfaces/RecentEpisode";
import AnimeService from "@util/consumet/AnimeService";
import Head from 'next/head'
import {useRecoilValue} from "recoil";

interface Props {
    randomAnime: Anime;
    recentEpisodes: RecentEpisode[];
    trendingAnime: Anime[];
    popularAnime: Anime[];
    genre1Anime: Anime[];
    genre2Anime: Anime[];
    genre3Anime: Anime[];
    genre4Anime: Anime[];
    randomGenres: Genres[];
}

export default function Home({
	 randomAnime,
	 recentEpisodes,
	 trendingAnime,
	 popularAnime,
	 genre1Anime,
	 genre2Anime,
	 genre3Anime,
	 genre4Anime,
	 randomGenres
 }: Props) {
    const anime = useRecoilValue(animeState);
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
                        <div className={"bg-gradient-to-b from-transparent via-black to-[#141414]"}>
                            <div className={"pt-[7.5%]"}>
                                {/*  RECENTLY ADDED ANIME CAROUSEL */}
                                <Row title="Recently Added" animes={recentEpisodes}/>
                            </div>
                        </div>
                        
                        <div className={"h-fit"}>
                            {/*  TRENDING ANIME CAROUSEL */}
                            <Row title="Trending" animes={trendingAnime}/>
                            
                            {/*  POPULAR ANIME CAROUSEL */}
                            <Row title="Popular" animes={popularAnime}/>
                            
                            {/*  GENRE 1 ANIME CAROUSEL */}
                            <Row title={randomGenres[0]} animes={genre1Anime}/>
                            
                            {/*  GENRE 2 ANIME CAROUSEL */}
                            <Row title={randomGenres[1]} animes={genre2Anime}/>
                            
                            {/*  GENRE 3 ANIME CAROUSEL */}
                            <Row title={randomGenres[2]} animes={genre3Anime}/>
                            
                            {/*  GENRE 4 ANIME CAROUSEL */}
                            <Row title={randomGenres[3]} animes={genre4Anime}/>
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
    const [recentEpisodes, trendingAnime, popularAnime, genre1Anime, genre2Anime, genre3Anime, genre4Anime] = await
        Promise.all([
            AnimeService.getRecentEpisodes(),
            AnimeService.getTrendingAnime(),
            AnimeService.getPopularAnime(),
            AnimeService.getAnimeByGenre(randomGenres[0]),
            AnimeService.getAnimeByGenre(randomGenres[1]),
            AnimeService.getAnimeByGenre(randomGenres[2]),
            AnimeService.getAnimeByGenre(randomGenres[3]),
        ])
    
    let randomAnime: Anime;
    let animeList: Anime[] = [...popularAnime, ...trendingAnime]
    do {
        randomAnime = animeList[Math.floor(Math.random() * (animeList.length - 1))];
    } while (!randomAnime.cover || !randomAnime.title)
    return {
        props: {
            randomAnime: randomAnime,
            recentEpisodes: recentEpisodes,
            trendingAnime: trendingAnime,
            popularAnime: popularAnime,
            genre1Anime: genre1Anime,
            genre2Anime: genre2Anime,
            genre3Anime: genre3Anime,
            genre4Anime: genre4Anime,
            randomGenres
        }
    }
}
