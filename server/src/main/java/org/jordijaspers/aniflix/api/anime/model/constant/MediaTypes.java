package org.jordijaspers.aniflix.api.anime.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum MediaTypes {

    TV("TV"),
    TV_SHORT("TV Short"),
    OVA("OVA"),
    ONA("ONA"),
    MOVIE("Movie"),
    SPECIAL("Special"),
    MUSIC("Music"),
    UNKNOWN("Unknown");

    private final String name;

    /**
     * @return A stream of the configured MediaTypes.
     */
    public static Stream<MediaTypes> stream() {
        return Stream.of(MediaTypes.values());
    }

    /**
     * @return A list of all configured MediaTypes.
     */
    public static List<MediaTypes> getAll() {
        return Arrays.asList(MediaTypes.values());
    }

    /**
     * Retrieves the enum value based on the given name. Ignoring case.
     */
    public static MediaTypes ofName(final String text) {
        return stream()
                .filter(mediaTypes -> mediaTypes.name().equalsIgnoreCase(text))
                .findFirst()
                .orElse(MediaTypes.UNKNOWN);
    }

}
