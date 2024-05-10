package org.jordijaspers.aniflix.api.anime.model.request;

import lombok.Data;
import org.jordijaspers.aniflix.api.anime.model.constant.AnimeSeason;
import org.jordijaspers.aniflix.api.anime.model.constant.Genres;

/**
 * The request for the anime.
 */
@Data
public class AnimeRequest {

    private int page;

    private int perPage;

    private String title;

    private Genres genre;

    private AnimeSeason season;

}
