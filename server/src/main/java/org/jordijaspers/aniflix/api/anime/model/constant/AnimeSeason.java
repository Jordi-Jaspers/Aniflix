package org.jordijaspers.aniflix.api.anime.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum AnimeSeason {

    UNKNOWN("Unknown"),
    WINTER("Winter"),
    SPRING("Spring"),
    SUMMER("Summer"),
    FALL("Fall");

    private final String name;

    /**
     * @return A stream of the configured AnimeStatus.
     */
    public static Stream<AnimeSeason> stream() {
        return Stream.of(AnimeSeason.values());
    }

    /**
     * @return A list of all configured AnimeStatus.
     */
    public static List<AnimeSeason> getAll() {
        return Arrays.asList(AnimeSeason.values());
    }

    /**
     * Retrieves the enum value based on the given name. Ignoring case.
     */
    public static AnimeSeason ofName(final String text) {
        return stream()
                .filter(animeSeason -> animeSeason.name().equalsIgnoreCase(text))
                .findFirst()
                .orElse(AnimeSeason.UNKNOWN);
    }
}
