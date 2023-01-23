import {Anime} from "@interfaces/Anime";
import {Episode} from "@interfaces/Episode";
import {MediaSources} from "@interfaces/MediaSources";
import {Page} from "@interfaces/Page";
import {RecentEpisode} from "@interfaces/RecentEpisode";
import ConsumetEndpoints from "@consumet/ConsumetEndpoints";
import {LOGGER} from "@util/Logger";
import RequestLogger from "@util/RequestLogger";

export default class AnimeService {
    
    /**
     * Returns the details of an Anime specified by its id.
     */
    static async getAnimeDetails(id: string): Promise<Anime> {
        const request = ConsumetEndpoints.ANIME_DETAILS.replace("{id}", id);
        RequestLogger.log(request, {});
        return await fetch(request).then((res) => res.json()).catch((err) => LOGGER.error(err));
    }
    
    /**
     * Returns a list of Anime TV series that match the given genre.
     *
     * @param genre     The genre to search for.
     * @param results   The number of results to return.
     * @param page      The page to return.
     */
    static async getAnimeByGenre(genre: string, results: number = 25, page: number = 1): Promise<Anime[]> {
        const request = ConsumetEndpoints.ANIME_BY_GENRE
            .replace("{genre}", genre)
            .replace("{page}", page.toString())
            .replace("{results}", results.toString());
    
        RequestLogger.log(request, {});
        const response: Page<Anime> = await fetch(request).then((res) => res.json()).catch((err) => LOGGER.error(err));
        return response.results;
    }
    
    /**
     * Returns a list of popular Anime TV series.
     *
     * @param results   The number of results to return.
     * @param page      The page to return.
     */
    static async getPopularAnime(results: number = 25, page: number = 1): Promise<Anime[]> {
        const request = ConsumetEndpoints.POPULAR_ANIME
            .replace("{page}", page.toString())
            .replace("{results}", results.toString());
    
        RequestLogger.log(request, {});
        const response: Page<Anime> = await fetch(request).then((res) => res.json()).catch((err) => LOGGER.error(err));
        return response.results;
    }
    
    /**
     * Returns a list of trending Anime TV series.
     *
     * @param results   The number of results to return.
     * @param page      The page to return.
     */
    static async getTrendingAnime(results: number = 25, page: number = 1): Promise<Anime[]> {
        const request = ConsumetEndpoints.TRENDING_ANIME
            .replace("{page}", page.toString())
            .replace("{results}", results.toString());
    
        RequestLogger.log(request, {});
        const response: Page<Anime> = await fetch(request).then((res) => res.json()).catch((err) => LOGGER.error(err));
        return response.results;
    }
    
    /**
     * Returns a list of recently released episodes of currently airing Anime TV series
     * by retrieving all the recent episodes and filtering out the ones that are chinese.
     *
     * @param results   The number of results to return.
     * @param page      The page to return.
     */
    static async getRecentEpisodes(results: number = 25, page: number = 1): Promise<RecentEpisode[]> {
        const request = ConsumetEndpoints.RECENT_EPISODES
            .replace("{page}", page.toString())
            .replace("{results}", results.toString());
        
        const regex = new RegExp(/[一-龠]+|[ぁ-ゔ]+|[ァ-ヴー]+|[a-zA-Z0-9]+|[ａ-ｚＡ-Ｚ０-９]+|[々〆〤ヶ]+/u);
        const response: Page<RecentEpisode> = await fetch(request).then((res) => res.json()).catch((err) => LOGGER.error(err));
        const recentAnime: RecentEpisode[] = response.results;
        for (let i = 0; i < recentAnime.length; i++) {
            const isValid: boolean = regex.test(recentAnime[i].title.native);
            if (!isValid) recentAnime.splice(i, 1);
        }
        RequestLogger.log(request, {});
        return recentAnime;
    }
    
    /**
     * Returns all the meta information for a specified episode by its id.
     *
     * @param id        The id of the anime.
     * @param episode   The number of the episode.
     */
    static async getEpisodeInformation(id: string, episode: number): Promise<Episode | null> {
        const request = ConsumetEndpoints.ANIME_DETAILS.replace("{id}", id);
        RequestLogger.log(request, {});
        const response: Anime = await fetch(request).then((res) => res.json()).catch((err) => LOGGER.error(err));
        const requestedEpisode: Episode | undefined = response.episodes.find((result: Episode) => result.number === episode);
        if (!requestedEpisode) {
            LOGGER.error(`Could not find episode number '%d' for the following anime:\n%o`, episode, response);
            return null;
        }
        return requestedEpisode;
    }
    
    /**
     * Returns all the media sources for a specified episode by its id.
     *
     * @param id        The id of the anime.
     * @param episode   The number of the episode.
     */
    static async getEpisodeLinks(id: string, episode: number): Promise<[Anime, Episode, MediaSources] | [null, null, null]> {
        const anime: Anime = await AnimeService.getAnimeDetails(id)
        const requestedEpisode: Episode | undefined = anime.episodes.find((result: Episode) => result.number === episode);
        if (!requestedEpisode) {
            LOGGER.error(`Could not find episode number '%d' for the following anime:\n%o`, episode, anime);
            return [null, null, null];
        }
        
        const request = ConsumetEndpoints.EPISODE_LINKS.replace("{episodeId}", requestedEpisode.id);
        RequestLogger.log(request, {});
        const mediaSources: MediaSources = await fetch(request).then((res) => res.json()).catch((err) => LOGGER.error(err));
        return [anime, requestedEpisode, mediaSources];
    }
}

