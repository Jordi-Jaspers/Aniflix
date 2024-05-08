package org.jordijaspers.aniflix.api.consumed.consumet.service;

import lombok.Getter;
import org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders;
import org.jordijaspers.aniflix.common.exception.AnilistProvidersDownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClientRequest;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Getter
@Component
public class DomainHealthChecker {

    /**
     * The default class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DomainHealthChecker.class);

    /**
     * Indicates whether any of the configured domains are reachable.
     */
    private boolean anyAvailableDomain = false;

    /**
     * The current active provider.
     */
    private AnilistProviders activeProvider = null;

    /**
     * The WebClient to use for the health checks.
     */
    private final WebClient client;

    /**
     * The default AllArgsConstructor.
     */
    public DomainHealthChecker(@Qualifier("consumetClient") final WebClient client) {
        this.client = client;
    }

    /**
     * Ping the configured Anilist Providers.
     */
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void ping() {
        LOGGER.info("[HealthCheck] Checking the health of the configured Anilist Providers.");
        Arrays.stream(AnilistProviders.values())
                .forEach(provider -> {
                    try {
                        client.get()
                                .uri(provider.getDomain())
                                .httpRequest(httpRequest -> {
                                    HttpClientRequest reactorRequest = httpRequest.getNativeRequest();
                                    reactorRequest.responseTimeout(Duration.ofMillis(500));
                                })
                                .retrieve()
                                .bodyToMono(Void.class)
                                .block();

                        anyAvailableDomain = true;
                        activeProvider = provider;
                    } catch (final Exception exception) {
                        LOGGER.error("[HealthCheck] No connection could be established with the provider '{}'.", provider.getProvider());
                        anyAvailableDomain = false;
                    }
                });
        LOGGER.info("[HealthCheck] Finished health check of the configured Anilist Providers. Active provider: '{}'.", activeProvider);
    }

    /**
     * Validate the availability of the Anilist Providers.
     */
    public void validateAvailability() {
        if (!anyAvailableDomain) {
            throw new AnilistProvidersDownException();
        }
    }
}
