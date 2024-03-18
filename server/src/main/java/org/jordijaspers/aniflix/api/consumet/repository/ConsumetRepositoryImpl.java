package org.jordijaspers.aniflix.api.consumet.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jordijaspers.aniflix.api.consumet.model.ResultPage;
import org.jordijaspers.aniflix.api.consumet.model.anilist.AnilistInfoResult;
import org.jordijaspers.aniflix.api.consumet.model.anilist.AnilistOverview;
import org.jordijaspers.aniflix.api.consumet.model.anilist.AnilistRecentEpisode;
import org.jordijaspers.aniflix.api.consumet.model.anilist.AnilistSearchResult;
import org.jordijaspers.aniflix.api.consumet.model.exception.ConsumetError;
import org.jordijaspers.aniflix.common.exception.ConsumetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;
import static org.jordijaspers.aniflix.api.consumet.ConsumetConstants.Endpoints.*;
import static org.jordijaspers.aniflix.api.consumet.ConsumetConstants.QueryParams.*;

@Repository
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class ConsumetRepositoryImpl implements ConsumetRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumetRepository.class);

    private final ObjectMapper objectMapper;

    private final WebClient client;

    public ConsumetRepositoryImpl(@Qualifier("consumetClient") final WebClient client, final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.client = client;
    }

    /**
     * Searches for an anime by its name.
     *
     * @param query The name of the anime.
     */
    @Override
    public ResultPage<AnilistSearchResult> searchAnime(final String query) {
        return searchAnime(Map.of(QUERY_PARAM, query));
    }

    /**
     * Searches for an anime by its name and apply any filters.
     *
     * @param filters The filters to apply.
     */
    @Override
    public ResultPage<AnilistSearchResult> searchAnime(final Map<String, String> filters) {
        return client.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(ADVANCED_SEARCH);
                    filters.forEach(uriBuilder::queryParam);
                    LOGGER.info("[Consumet API] Invoking Consumet API with the following URL: '{}'", uriBuilder.build());
                    return uriBuilder.build();
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleConsumetError)
                .bodyToMono(new ParameterizedTypeReference<ResultPage<AnilistSearchResult>>() {

                })
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    /**
     * Returns the details of an Anime specified by its id.
     */
    @Override
    public AnilistInfoResult getAnimeDetails(final int id) {
        return client.get()
                .uri(uriBuilder -> {
                    final URI uri = uriBuilder
                            .path(ANIME_DETAILS)
                            .build(id);
                    LOGGER.info("[Consumet API] Invoking Consumet API with the following URL: '{}'", uri);
                    return uri;
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleConsumetError)
                .bodyToMono(AnilistInfoResult.class)
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    /**
     * Returns a list of popular Anime TV series.
     *
     * @param results The number of results to return.
     * @param page    The page to return.
     */

    @Override
    public ResultPage<AnilistOverview> getPopularAnime(final int results, final int page) {
        return client.get()
                .uri(uriBuilder -> {
                    final URI uri = uriBuilder
                            .path(POPULAR_ANIME)
                            .queryParam(PAGE_PARAM, page)
                            .queryParam(PER_PAGE_PARAM, results)
                            .build();
                    LOGGER.info("[Consumet API] Invoking Consumet API with the following URL: '{}'", uri);
                    return uri;
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleConsumetError)
                .bodyToMono(new ParameterizedTypeReference<ResultPage<AnilistOverview>>() {

                })
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    /**
     * Returns a list of trending Anime TV series.
     *
     * @param results The number of results to return.
     * @param page    The page to return.
     */
    @Override
    public ResultPage<AnilistOverview> getTrendingAnime(final int results, final int page) {
        return client.get()
                .uri(uriBuilder -> {
                    final URI uri = uriBuilder
                            .path(TRENDING_ANIME)
                            .queryParam(PAGE_PARAM, page)
                            .queryParam(PER_PAGE_PARAM, results)
                            .build();
                    LOGGER.info("[Consumet API] Invoking Consumet API with the following URL: '{}'", uri);
                    return uri;
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleConsumetError)
                .bodyToMono(new ParameterizedTypeReference<ResultPage<AnilistOverview>>() {

                })
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    /**
     * Returns a list of recently released episodes of currently airing Anime TV series
     * by retrieving all the recent episodes and filtering out the ones that are chinese.
     *
     * @param results The number of results to return.
     * @param page    The page to return.
     */
    @Override
    public ResultPage<AnilistRecentEpisode> getRecentEpisodes(final int results, final int page) {
        return client.get()
                .uri(uriBuilder -> {
                    final URI uri = uriBuilder
                            .path(RECENT_EPISODES)
                            .queryParam(PAGE_PARAM, page)
                            .queryParam(PER_PAGE_PARAM, results)
                            .build();
                    LOGGER.info("[Consumet API] Invoking Consumet API with the following URL: '{}'", uri);
                    return uri;
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleConsumetError)
                .bodyToMono(new ParameterizedTypeReference<ResultPage<AnilistRecentEpisode>>() {

                })
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    /**
     * Returns a list of Anime TV series that match the given genre.
     *
     * @param genre   The genre to search for.
     * @param results The number of results to return.
     * @param page    The page to return.
     */
    @Override
    public ResultPage<AnilistOverview> getAnimeByGenre(final String genre, final int results, final int page) {
        return client.get()
                .uri(uriBuilder -> {
                    final URI uri = uriBuilder
                            .path(ADVANCED_SEARCH)
                            .queryParam(TYPE_PARAM, "ANIME")
                            .queryParam(SORT_PARAM, "[\"SCORE_DESC\",\"UPDATED_AT_DESC\",\"TRENDING_DESC\"]")
                            .queryParam(GENRES_PARAM, "[\"" + genre + "\"]")
                            .queryParam(PAGE_PARAM, page)
                            .queryParam(PER_PAGE_PARAM, results)
                            .build();
                    LOGGER.info("Calling Consumet API with the following URL: '{}'", uri);
                    return uri;
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleConsumetError)
                .bodyToMono(new ParameterizedTypeReference<ResultPage<AnilistOverview>>() {

                })
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    /**
     * Returns all the meta information for a specified episode by its id.
     *
     * @param id      The id of the anime.
     * @param episode The number of the episode.
     */
    @Override
    public ResultPage<AnilistOverview> getEpisodeInformation(final String id, final int episode) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns all the media sources for a specified episode by its id.
     *
     * @param id      The id of the anime.
     * @param episode The number of the episode.
     */
    @Override
    public ResultPage<AnilistOverview> getEpisodeLinks(final String id, final int episode) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // ======================== PRIVATE METHODS ========================

    private Mono<Throwable> handleConsumetError(final ClientResponse clientResponse) {
        LOGGER.error("[Consumet API] Received an error response from the server with status code '{}'", clientResponse.statusCode());
        return clientResponse.bodyToMono(String.class)
                .flatMap(response -> {
                    final Mono<Throwable> error = Mono.error(new ConsumetException(response));
                    try {
                        final ConsumetError errorResponse = objectMapper.readValue(response, ConsumetError.class);
                        if (nonNull(errorResponse)) {
                            LOGGER.error("[Consumet API] Consumet API returned an error: '{}'", errorResponse.getMessage());
                            return Mono.error(new ConsumetException(errorResponse.getMessage()));
                        }
                    } catch (final JsonProcessingException noConsumetError) {
                        LOGGER.error("[Consumet API] Unknown error response from Consumet API: '{}'", response);
                    }
                    return error;
                });
    }

    private static Consumer<Throwable> onObjectMappingErrorLog() {
        return throwable -> LOGGER.error("[Consumet API] Error while mapping the results to a domain:\n", throwable.getCause());
    }
}
