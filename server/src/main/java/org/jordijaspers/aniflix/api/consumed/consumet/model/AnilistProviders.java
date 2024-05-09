package org.jordijaspers.aniflix.api.consumed.consumet.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum AnilistProviders {

    GOGOANIME("gogoanime", "https://anitaku.to"),
    ZORO("zoro", "https://hianime.to");

    private final String provider;

    private final String domain;

    /**
     * @return all the domains of the providers, sorted by their ordinal.
     */
    public static List<String> getDomains() {
        return Stream.of(values())
                .sorted(Comparator.comparingInt(Enum::ordinal))
                .map(AnilistProviders::getDomain)
                .toList();
    }

    /**
     * Retrieve the provider based on the given domain.
     */
    public static AnilistProviders getProviderByDomain(final String domain) {
        for (AnilistProviders provider : values()) {
            if (provider.getDomain().equalsIgnoreCase(domain)) {
                return provider;
            }
        }
        return null;
    }
}
