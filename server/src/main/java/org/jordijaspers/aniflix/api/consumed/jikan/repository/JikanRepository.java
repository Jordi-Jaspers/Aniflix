package org.jordijaspers.aniflix.api.consumed.jikan.repository;

import net.sandrohc.jikan.model.anime.Anime;
import net.sandrohc.jikan.model.anime.AnimeEpisode;
import net.sandrohc.jikan.model.common.RecommendationSimple;
import net.sandrohc.jikan.model.common.Trailer;

import java.util.List;
import java.util.Map;

/**
 * The repository for the Jikan API.
 */
public interface JikanRepository {

    /**
     * Returns the details of an Anime specified by its Mal id.
     */
    Anime getAnimeDetails(int id);

    /**
     * Returns the trailer of an Anime specified by its Mal id.
     */
    Trailer getAnimeTrailer(int id);

    /**
     * Returns the recommendations of an Anime specified by its Mal id.
     */
    List<RecommendationSimple> getAnimeRecommendations(int id);

    /**
     * Returns all the episode information of an Anime specified by its Mal id.
     */
    Map<Integer, AnimeEpisode> getAnimeEpisodes(int id, int totalEpisodes);
}
