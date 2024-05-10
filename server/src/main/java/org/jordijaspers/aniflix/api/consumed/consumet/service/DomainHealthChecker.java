package org.jordijaspers.aniflix.api.consumed.consumet.service;

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
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;
import static org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders.NO_PROVIDER;

/**
 * The DomainHealthChecker is responsible for checking the health of the configured Anilist Providers.
 */
@Component
public class DomainHealthChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainHealthChecker.class);

    private static AnilistProviders activeProvider = NO_PROVIDER;

    private final WebClient client;

    public DomainHealthChecker(@Qualifier("consumetClient") final WebClient client) {
        this.client = client;
    }

    /**
     * Ping the configured Anilist Providers.
     */
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void ping() {
        LOGGER.info("[HealthCheck] Checking the health of the configured Anilist Providers.");
        AnilistProviders.getDomains().stream()
                .filter(domain -> {
                    try {
                        LOGGER.info("[HealthCheck] Pinging provider's domain '{}'.", domain);
                        client.get()
                                .uri(domain)
                                .httpRequest(httpRequest -> {
                                    final HttpClientRequest reactorRequest = httpRequest.getNativeRequest();
                                    reactorRequest.responseTimeout(Duration.ofMillis(500));
                                })
                                .retrieve()
                                .bodyToMono(Void.class)
                                .block();

                        return true;
                    } catch (final Exception exception) {
                        LOGGER.error("[HealthCheck] No connection could be established with the domain '{}'.", domain);
                    }
                    return false;
                })
                .findFirst()
                .ifPresentOrElse(
                        provider -> activeProvider = AnilistProviders.getProviderByDomain(provider),
                        () -> activeProvider = NO_PROVIDER
                );

        LOGGER.info("[HealthCheck] Finished health check of the configured Anilist Providers. Active provider: '{}'.", activeProvider);
    }

    /**
     * Validate the availability of the Anilist Providers.
     */
    public static void validateAvailability() {
        if (isNull(activeProvider) || activeProvider == NO_PROVIDER) {
            throw new AnilistProvidersDownException();
        }
    }

    /**
     * @return the active provider if available, else throws an exception.
     */
    public static AnilistProviders getActiveProvider() {
        validateAvailability();
        return activeProvider;
    }
}
