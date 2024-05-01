package org.jordijaspers.aniflix.api.consumed.jikan.repository;

import net.sandrohc.jikan.model.anime.Anime;
import net.sandrohc.jikan.model.anime.AnimeEpisode;

import java.util.Map;

public interface JikanRepository {

    /**
     * Returns the details of an Anime specified by its Mal id.
     */
    Anime getAnimeDetails(int id);

    /**
     * Returns all the episode information of an Anime specified by its Mal id.
     */
    Map<Integer, AnimeEpisode> getAnimeEpisodes(int id);
}
