package org.jordijaspers.aniflix.api.anime.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

/**
 * An enum to represent the different seasons of the year.
 */
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
    public static List<String> getAll() {
        return stream()
                .filter(value -> !value.equals(UNKNOWN))
                .map(Enum::name)
                .toList();
    }

    /**
     * Retrieves the enum value based on the given name. Ignoring case.
     */
    public static AnimeSeason ofName(final String text) {
        return stream()
                .filter(animeSeason -> animeSeason.getName().equalsIgnoreCase(text))
                .findFirst()
                .orElse(AnimeSeason.UNKNOWN);
    }
}
