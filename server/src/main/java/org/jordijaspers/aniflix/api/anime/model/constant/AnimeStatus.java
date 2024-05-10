package org.jordijaspers.aniflix.api.anime.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

/**
 * The status of an anime.
 */
@Getter
@RequiredArgsConstructor
public enum AnimeStatus {

    UNKNOWN("Unknown"),
    ONGOING("Ongoing"),
    COMPLETED("Completed"),
    NOT_YET_AIRED("Not yet aired");

    private final String name;

    /**
     * @return A stream of the configured AnimeStatus.
     */
    public static Stream<AnimeStatus> stream() {
        return Stream.of(AnimeStatus.values());
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
    public static AnimeStatus ofName(final String text) {
        return stream()
                .filter(animeStatus -> animeStatus.getName().equalsIgnoreCase(text))
                .findFirst()
                .orElse(AnimeStatus.UNKNOWN);
    }
}
