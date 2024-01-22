package org.jordijaspers.aniflix.api.anime.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum AnimeStatus {

    ONGOING("Ongoing"),
    COMPLETED("Completed"),
    NOT_YET_AIRED("Not yet aired"),
    UNKNOWN("Unknown");

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
    public static List<AnimeStatus> getAll() {
        return Arrays.asList(AnimeStatus.values());
    }

    /**
     * Retrieves the enum value based on the given name. Ignoring case.
     */
    public static AnimeStatus ofName(final String text) {
        return stream()
                .filter(animeStatus -> animeStatus.name().equalsIgnoreCase(text))
                .findFirst()
                .orElse(AnimeStatus.UNKNOWN);
    }
}
