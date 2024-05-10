package org.jordijaspers.aniflix.api.anime.model.request;

import lombok.Data;
import org.jordijaspers.aniflix.api.anime.model.constant.Genres;

/**
 * The request for the genres.
 */
@Data
public class GenreRequest {

    private int page;

    private int perPage;

    private Genres genre;

    public void setGenre(final String genre) {
        this.genre = Genres.ofName(genre);
    }
}
