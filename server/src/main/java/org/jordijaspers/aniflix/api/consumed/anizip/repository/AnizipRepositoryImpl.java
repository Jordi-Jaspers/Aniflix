package org.jordijaspers.aniflix.api.consumed.anizip.repository;

import org.jordijaspers.aniflix.api.consumed.anizip.model.AnizipInfo;
import org.jordijaspers.aniflix.common.exception.ConsumetAPIException;
import org.jordijaspers.aniflix.common.util.logging.LogExecutionTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

/**
 * The implementation of the {@link AnizipRepository} interface.
 */
@Repository
public class AnizipRepositoryImpl implements AnizipRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnizipRepositoryImpl.class);

    private final WebClient client;

    public AnizipRepositoryImpl(@Qualifier("anizipClient") final WebClient client) {
        this.client = client;
    }

    private static Consumer<Throwable> onObjectMappingErrorLog() {
        return throwable -> LOGGER.error("[Anizip API] Error while mapping the results to a domain:\n", throwable.getCause());
    }

    @Override
    @LogExecutionTime
    public AnizipInfo getAnizipInfoByMalId(final int id) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("mal_id", id)
                        .path("/mappings")
                        .build(id)
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleAnizipError)
                .bodyToMono(AnizipInfo.class)
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    // ======================== PRIVATE METHODS ========================

    @Override
    @LogExecutionTime
    public AnizipInfo getAnizipInfoByAniListId(final int id) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("anilist_id", id)
                        .path("/mappings")
                        .build(id)
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleAnizipError)
                .bodyToMono(AnizipInfo.class)
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    private Mono<Throwable> handleAnizipError(final ClientResponse clientResponse) {
        LOGGER.error("[Anizip API] Received an error response from the server with status code '{}'", clientResponse.statusCode());
        return clientResponse.bodyToMono(String.class)
                .flatMap(response -> Mono.error(new ConsumetAPIException(response)));
    }
}
