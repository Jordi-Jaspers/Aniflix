import {Anime} from "@interfaces/Anime";
import {GoEpisode} from "@interfaces/GoEpisode";
import {Page} from "@interfaces/Page";
import ConsumetApi from "@utils/ConsumetApi";

/**
 * Returns a list of Anime by genre.
 */
export async function getAnimeByGenre(genre: string): Promise<Anime[]> {
    const response: Page<Anime> = await fetch(ConsumetApi.fetchGenre.replace("{genre}", genre)).then((res) => res.json());
    console.log(`INFO: invoked '${ConsumetApi.fetchGenre}' with following response: `, response.hasNextPage ? response.results.length : response);
    return response.results;

}

/**
 * Returns a list of popular Anime.
 */
export async function getPopularAnime(): Promise<Anime[]> {
    const response: Page<Anime> = await fetch(ConsumetApi.fetchPopularAnime).then((res) => res.json());
    console.log(`INFO: invoked '${ConsumetApi.fetchPopularAnime}' with following response: `, response.hasNextPage ? response.results.length : response);
    return response.results;
}

/**
 * Returns a list of trending Anime.
 */
export async function getTrendingAnime(): Promise<Anime[]> {
    const response: Page<Anime> = await fetch(ConsumetApi.fetchTrending).then((res) => res.json());
    console.log(`INFO: invoked '${ConsumetApi.fetchTrending}' with following response: `, response.hasNextPage ? response.results.length : response);
    return response.results;
}

/**
 * Returns a list of recently released episodes on GogoAnime and provisions the object with detailed information from Anilist.
 */
export async function getRecentEpisodes(): Promise<Anime[]> {
    const recentEpisodesPage: Page<GoEpisode> = await fetch(ConsumetApi.fetchRecentEpisodesGogoAnime).then((res) => res.json());
    const recentEpisodes: GoEpisode[] = recentEpisodesPage.results;
    const response: Promise<Anime|GoEpisode>[] = recentEpisodes.map(async (episode: GoEpisode) => {
        const title: string = episode.title.split(":")[0];
        const url: string = `${ConsumetApi.advancedAnimeSearch}?sort=["ID_DESC", "UPDATED_AT_DESC"]&perPage=1&type=ANIME&format=TV&query=${title}`;
        const animePage: Page<Anime> = await fetch(url).then((res) => res.json());

        if (!animePage.results) {
            return episode;
        } else {
            const anime: Anime = animePage.results[0];
            anime.recentEpisode = episode;
            return anime
        }
    });
    console.log(`INFO: invoked '${ConsumetApi.fetchRecentEpisodesGogoAnime}' with following response: `, recentEpisodesPage.hasNextPage ? recentEpisodesPage.results.length : recentEpisodesPage);
    return (await Promise.all(response)).filter((anime: Anime|GoEpisode) => anime.hasOwnProperty("recentEpisode")) as Anime[];
}
