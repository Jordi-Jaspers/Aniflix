package org.jordijaspers.aniflix.api.consumed.consumet.repository;

import org.jordijaspers.aniflix.api.consumed.consumet.model.ResultPage;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistInfoResult;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistNewsFeed;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistNewsPost;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistOverview;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistRecentEpisode;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistRecommendation;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistSearchResult;

import java.util.List;
import java.util.Map;

public interface ConsumetRepository {

    /**
     * Searches for an anime by its name.
     *
     * @param query The name of the anime.
     */
    ResultPage<AnilistSearchResult> searchAnime(String query);

    /**
     * Searches for an anime and apply any filters.
     *
     * @param filters The filters to apply.
     */
    ResultPage<AnilistSearchResult> searchAnime(Map<String, String> filters);

    /**
     * Returns the details of an Anime specified by its id, with the episodes.
     */
    AnilistInfoResult getAnimeDetails(int id);

    /**
     * Returns the recommendations for an Anime specified by its id.
     */
    List<AnilistRecommendation> getAnimeRecommendations(int id);

    /**
     * Returns a list of popular Anime TV series.
     *
     * @param results The number of results to return.
     * @param page    The page to return.
     */
    ResultPage<AnilistOverview> getPopularAnime(int results, int page);

    /**
     * Returns a list of trending Anime TV series.
     *
     * @param results The number of results to return.
     * @param page    The page to return.
     */
    ResultPage<AnilistOverview> getTrendingAnime(int results, int page);

    /**
     * Returns a list of recently released episodes of currently airing Anime TV series
     * by retrieving all the recent episodes and filtering out the ones that are chinese.
     * <p>
     * TODO: Should be changed to '/meta/anilist/recent-episodes' when the API is updated.
     *
     * @param results The number of results to return.
     * @param page    The page to return.
     */
    ResultPage<AnilistRecentEpisode> getRecentEpisodes(int results, int page);

    /**
     * Returns a list of Anime TV series that match the given genre.
     *
     * @param genre   The genre to search for.
     * @param results The number of results to return.
     * @param page    The page to return.
     */
    ResultPage<AnilistOverview> getAnimeByGenre(String genre, int results, int page);

    /**
     * Returns all the meta information for a specified episode by its id.
     *
     * @param id      The id of the anime.
     * @param episode The number of the episode.
     */
    ResultPage<AnilistOverview> getEpisodeInformation(String id, int episode);

    /**
     * Returns all the media sources for a specified episode by its id.
     *
     * @param id      The id of the anime.
     * @param episode The number of the episode.
     */
    ResultPage<AnilistOverview> getEpisodeLinks(String id, int episode);

    /**
     * Returns the latest news articles.
     */
    List<AnilistNewsFeed> getNewsFeed();

    /**
     * Returns the details of a news article specified by its id.
     *
     * @param id The id of the news article.
     */
    AnilistNewsPost getNewsPost(String id);
}
