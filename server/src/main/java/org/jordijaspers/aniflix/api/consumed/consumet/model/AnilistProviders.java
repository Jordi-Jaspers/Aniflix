package org.jordijaspers.aniflix.api.consumed.consumet.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * The providers for the Anilist API with their respective domains.
 */
@Getter
@RequiredArgsConstructor
public enum AnilistProviders {

    NO_PROVIDER("NO_PROVIDER", "NO_DOMAIN"),
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
                .filter(provider -> !provider.equals(NO_PROVIDER))
                .map(AnilistProviders::getDomain)
                .toList();
    }

    /**
     * Retrieve the provider based on the given domain.
     */
    public static AnilistProviders getProviderByDomain(final String domain) {
        return Arrays.stream(values())
                .filter(provider -> provider.getDomain().equalsIgnoreCase(domain))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieve the provider based on the given provider.
     */
    public static AnilistProviders getProviderByProvider(final String provider) {
        return Arrays.stream(values())
                .filter(providers -> providers.getProvider().equalsIgnoreCase(provider))
                .findFirst()
                .orElse(null);
    }
}
