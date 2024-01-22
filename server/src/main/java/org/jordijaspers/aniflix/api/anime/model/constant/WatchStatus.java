package org.jordijaspers.aniflix.api.anime.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum WatchStatus {

    NOT_STARTED,
    WATCHING,
    COMPLETED;

    /**
     * @return A stream of the configured WatchStatus.
     */
    public static Stream<WatchStatus> stream() {
        return Stream.of(WatchStatus.values());
    }

    /**
     * @return A list of all configured WatchStatus.
     */
    public static List<WatchStatus> getAll() {
        return Arrays.asList(WatchStatus.values());
    }

    /**
     * Retrieves the enum value based on the given name. Ignoring case.
     */
    public static WatchStatus ofName(final String text) {
        return stream()
                .filter(watchStatus -> watchStatus.name().equalsIgnoreCase(text))
                .findFirst()
                .orElse(WatchStatus.NOT_STARTED);
    }
}
