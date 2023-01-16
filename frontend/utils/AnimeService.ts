import {Anime} from "@interfaces/Anime";
import {Episode} from "@interfaces/Episode";
import {GoEpisode} from "@interfaces/GoEpisode";
import {MediaSources} from "@interfaces/MediaSources";
import {Page} from "@interfaces/Page";
import ConsumetApi from "@utils/ConsumetApi";

/**
 * Returns a list of Anime by genre.
 */
export async function getAnimeByGenre(genre: string): Promise<Anime[]> {
    const response: Page<Anime> = await fetch(ConsumetApi.fetchGenre.replace("{genre}", genre)).then((res) => res.json());
    console.info(`invoked '${ConsumetApi.fetchGenre.replace("{genre}", genre)}' with following response: `, response.hasNextPage ? response.results.length : response);
    return response.results;

}

/**
 * Returns a list of popular Anime.
 */
export async function getPopularAnime(): Promise<Anime[]> {
    const response: Page<Anime> = await fetch(ConsumetApi.fetchPopularAnime).then((res) => res.json());
    console.info(`invoked '${ConsumetApi.fetchPopularAnime}' with following response: `, response.hasNextPage ? response.results.length : response);
    return response.results;
}

/**
 * Returns a list of trending Anime.
 */
export async function getTrendingAnime(): Promise<Anime[]> {
    const response: Page<Anime> = await fetch(ConsumetApi.fetchTrending).then((res) => res.json());
    console.info(`invoked '${ConsumetApi.fetchTrending}' with following response: `, response.hasNextPage ? response.results.length : response);
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
    console.info(`invoked '${ConsumetApi.fetchRecentEpisodesGogoAnime}' with following response: `, recentEpisodesPage.hasNextPage ? recentEpisodesPage.results.length : recentEpisodesPage);
    return (await Promise.all(response)).filter((anime: Anime|GoEpisode) => anime.hasOwnProperty("recentEpisode")) as Anime[];
}

export async function getEpisodeInformation(id: string, episodeId: string): Promise<Episode | undefined> {
    const anime: Anime = await fetch(ConsumetApi.fetchAnimeDetails.replace("{id}", id)).then((res) => res.json());
    const episode: Episode | undefined = anime.episodes.find((episode: Episode) => episode.id === episodeId);
    console.info(`invoked '${ConsumetApi.fetchAnimeDetails.replace("{id}", id)}' with following response: `, anime);
    return episode;
}

export async function getEpisodeLinks(episodeId: string): Promise<MediaSources | undefined> {
    const response: MediaSources | undefined = await fetch(ConsumetApi.fetchEpisodeLinks.replace("{episodeId}", episodeId)).then((res) => res.json());
    console.info(`invoked '${ConsumetApi.fetchEpisodeLinks.replace("{episodeId}", episodeId)}' with following response: `, response);
    return response;
}
