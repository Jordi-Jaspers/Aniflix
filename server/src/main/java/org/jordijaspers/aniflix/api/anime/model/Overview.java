package org.jordijaspers.aniflix.api.anime.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jordijaspers.aniflix.api.genre.model.Genre;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Overview {

    /**
     * The recently added anime.
     */
    private List<Anime> recentlyAdded;

    /**
     * The anime that are currently airing.
     */
    private List<Anime> trending;

    /**
     * The anime that are currently airing.
     */
    private List<Anime> popular;

    /**
     * List of random anime sorted by Genre.
     */
    private Map<Genre, List<Anime>> genre;

}
