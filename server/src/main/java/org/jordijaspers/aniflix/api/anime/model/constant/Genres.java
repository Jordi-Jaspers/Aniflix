package org.jordijaspers.aniflix.api.anime.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.genre.model.Genre;

import java.util.List;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Genres {

    UNKNOWN(1, "Unknown"),
    ACTION(2, "Action"),
    ADVENTURE(3, "Adventure"),
    COMEDY(4, "Comedy"),
    DRAMA(5, "Drama"),
    FANTASY(6, "Fantasy"),
    HORROR(7, "Horror"),
    MECHA(8, "Mecha"),
    MUSIC(9, "Music"),
    MYSTERY(10, "Mystery"),
    PSYCHOLOGICAL(11, "Psychological"),
    ROMANCE(12, "Romance"),
    SLICE_OF_LIFE(13, "Slice of Life"),
    SPORTS(14, "Sports"),
    SUPERNATURAL(15, "Supernatural"),
    THRILLER(16, "Thriller");

    private final int id;

    private final String name;

    /**
     * @return A stream of the configured Genres.
     */
    public static Stream<Genres> stream() {
        return Stream.of(Genres.values());
    }

    /**
     * @return A list of all configured Genres, excluding UNKNOWN.
     */
    public static List<String> getAll() {
        return stream()
                .filter(value -> !value.equals(UNKNOWN))
                .map(Genres::getName)
                .toList();
    }

    /**
     * Tries to find a Genre by its name, else returns UNKNOWN.
     */
    public static Genres enumOf(final String value) {
        try {
            return Genres.valueOf(value.toUpperCase());
        } catch (final IllegalArgumentException exception) {
            return Genres.UNKNOWN;
        }
    }

    /**
     * Retrieves N amount of random Genres. If the amount is higher than the amount of Genres, it will return all Genres.
     */
    public static List<Genre> getRandomGenres(final int amount) {
        return stream()
                .skip((long) (Math.random() * (Genres.values().length - amount)))
                .limit(amount)
                .map(Genre::new)
                .toList();
    }

    /**
     * Retrieves the enum value based on the given name. Ignoring case.
     */
    public static Genres ofName(final String text) {
        return stream()
                .filter(genres -> genres.getName().equalsIgnoreCase(text))
                .findFirst()
                .orElse(Genres.UNKNOWN);
    }
}
