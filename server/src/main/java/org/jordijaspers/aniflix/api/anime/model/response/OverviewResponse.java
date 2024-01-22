package org.jordijaspers.aniflix.api.anime.model.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class OverviewResponse {

    /**
     * The recently added anime.
     */
    private List<AnimeResponse> recentlyAdded = new ArrayList<>();

    /**
     * The anime that are currently airing.
     */
    private List<AnimeResponse> trending = new ArrayList<>();

    /**
     * The anime that are currently airing.
     */
    private List<AnimeResponse> popular = new ArrayList<>();

    /**
     * List of random anime sorted by Genre.
     */
    private Map<String, List<AnimeResponse>> genre = new ConcurrentHashMap<>();

}
