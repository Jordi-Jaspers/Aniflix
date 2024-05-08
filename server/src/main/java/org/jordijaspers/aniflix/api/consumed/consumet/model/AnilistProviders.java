package org.jordijaspers.aniflix.api.consumed.consumet.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum AnilistProviders {

    GOGOANIME("gogoanime", "https://anitaku.to"),
    ZORO("zoro", "https://hianime.to");

    private final String provider;

    private final String domain;

    /**
     * @return A list of all the possible providers.
     */
    public static List<String> getProviders() {
        return List.of(GOGOANIME.getProvider(), ZORO.getProvider());
    }

    /**
     * @return all the domains of the providers.
     */
    public static List<String> getDomains() {
        return List.of(GOGOANIME.getDomain(), ZORO.getDomain());
    }
}
