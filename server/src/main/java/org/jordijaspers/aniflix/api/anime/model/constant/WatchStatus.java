package org.jordijaspers.aniflix.api.anime.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

/**
 * The watch status of an anime.
 */
@Getter
@RequiredArgsConstructor
public enum WatchStatus {

    NOT_STARTED("Not started"),
    WATCHING("Watching"),
    COMPLETED("Completed");

    private final String value;

    /**
     * @return A stream of the configured WatchStatus.
     */
    public static Stream<WatchStatus> stream() {
        return Stream.of(WatchStatus.values());
    }

    /**
     * @return A list of all configured WatchStatus.
     */
    public static List<String> getAll() {
        return stream().map(Enum::name).toList();
    }

}
