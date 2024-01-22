package org.jordijaspers.aniflix.api.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Authority {

    USER("The default authority for a user with limited permissions."),
    SUBSCRIBER("The authority for a user with a subscription."),
    ADMIN("The authority for a user with administrative permissions.");

    /**
     * The reason.
     */
    private final String description;

    /**
     * @return A stream of the configured permissions.
     */
    public static Stream<Authority> stream() {
        return Stream.of(Authority.values());
    }

    /**
     * @return A list of all configured permissions.
     */
    public static List<Authority> getAll() {
        return Arrays.asList(Authority.values());
    }
}
