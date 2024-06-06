package org.jordijaspers.aniflix.api.consumed.anizip.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

/**
 * The type of cover of an anime.
 */
@Getter
@RequiredArgsConstructor
public enum AnizipCoverType {

    BANNER("Banner"),
    POSTER("Poster"),
    FANART("Fanart"),
    CLEARLOGO("Clearlogo"),
    UNKNOWN("Unknown");

    private final String name;

    /**
     * @return A stream of the configured AnimeStatus.
     */
    public static Stream<AnizipCoverType> stream() {
        return Stream.of(AnizipCoverType.values());
    }

    /**
     * @return A list of all configured AnimeStatus.
     */
    public static List<String> getAll() {
        return stream()
                .filter(value -> !value.equals(UNKNOWN))
                .map(Enum::name)
                .toList();
    }

    /**
     * Retrieves the enum value based on the given name. Ignoring case.
     */
    public static AnizipCoverType ofName(final String text) {
        return stream()
                .filter(animeStatus -> animeStatus.getName().equalsIgnoreCase(text))
                .findFirst()
                .orElse(AnizipCoverType.UNKNOWN);
    }
}
