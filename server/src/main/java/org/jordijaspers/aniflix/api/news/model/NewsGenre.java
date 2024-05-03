package org.jordijaspers.aniflix.api.news.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum NewsGenre {

    GENERAL("general"),
    ANIME("anime"),
    MANGA("manga"),
    GAMES("games"),
    LIVE_ACTION("live-action"),
    EVENTS("events"),
    PEOPLE("people"),
    NOVELS("novels"),
    INDUSTRY("industry"),
    MUSIC("music");

    private final String genre;

    /**
     * @return A stream of the configured AnimeStatus.
     */
    public static Stream<NewsGenre> stream() {
        return Stream.of(NewsGenre.values());
    }

    /**
     * @return A list of all configured AnimeStatus.
     */
    public static List<String> getAll() {
        return stream()
                .map(Enum::name)
                .toList();
    }

    /**
     * Retrieve a comma-separated list of all genres and quote them.
     */
    public static String getCommaSeparatedGenres() {
        return stream()
                .map(NewsGenre::getGenre)
                .map(genre -> "\"" + genre + "\"")
                .reduce((genre1, genre2) -> genre1 + "," + genre2)
                .orElse("");
    }

    /**
     * Retrieves the enum value based on the given name. Ignoring case.
     */
    public static NewsGenre ofName(final String text) {
        return stream()
                .filter(genre -> genre.getGenre().equalsIgnoreCase(text))
                .findFirst()
                .orElse(NewsGenre.GENERAL);
    }
}
