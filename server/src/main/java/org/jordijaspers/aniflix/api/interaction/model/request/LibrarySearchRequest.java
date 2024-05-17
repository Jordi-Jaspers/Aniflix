package org.jordijaspers.aniflix.api.interaction.model.request;

import lombok.Data;
import lombok.ToString;
import org.jordijaspers.aniflix.api.anime.model.constant.AnimeStatus;
import org.jordijaspers.aniflix.api.anime.model.constant.Genres;
import org.jordijaspers.aniflix.api.anime.model.constant.WatchStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A request model for searching in the library.
 */
@Data
@ToString
public class LibrarySearchRequest {

    private int page;

    private int pageSize;

    private String query;

    private List<WatchStatus> watchStatus;

    private List<Genres> genre;

    private List<AnimeStatus> status;

    private double minRating;

    private double maxRating;

    private int afterYear;

    private int beforeYear;

    public void setGenre(final List<String> genres) {
        this.genre = genres.stream()
                .map(Genres::ofName)
                .toList();
    }
}
