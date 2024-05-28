package org.jordijaspers.aniflix.api.consumed.consumet.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jordijaspers.aniflix.api.consumed.consumet.model.ResultPage;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistInfoResult;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistNewsFeed;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistNewsPost;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistNextAiringEpisode;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistOverview;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistRecentEpisode;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistRecommendation;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistSearchResult;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistStreamingLinks;
import org.jordijaspers.aniflix.api.consumed.consumet.model.exception.ConsumetError;
import org.jordijaspers.aniflix.api.news.model.NewsGenre;
import org.jordijaspers.aniflix.common.exception.ConsumetAPIException;
import org.jordijaspers.aniflix.common.util.logging.LogExecutionTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.jordijaspers.aniflix.api.consumed.consumet.ConsumetConstants.Endpoints.*;
import static org.jordijaspers.aniflix.api.consumed.consumet.ConsumetConstants.QueryParams.*;
import static org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders.ZORO;
import static org.jordijaspers.aniflix.api.consumed.consumet.service.DomainHealthChecker.getActiveProvider;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * The implementation of the {@link ConsumetRepository} interface.
 */
@Repository
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class ConsumetRepositoryImpl implements ConsumetRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumetRepository.class);

    private final ObjectMapper objectMapper;

    private final WebClient client;

    public ConsumetRepositoryImpl(@Qualifier("consumetClient") final WebClient client, final ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public ResultPage<AnilistSearchResult> searchAnime(final String query) {
        return searchAnime(Map.of(QUERY_PARAM, query));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public ResultPage<AnilistSearchResult> searchAnime(final Map<String, String> filters) {
        return client.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(ADVANCED_SEARCH);
                    filters.forEach(uriBuilder::queryParam);
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
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public AnilistInfoResult getAnimeDetails(final int id) {
        return getAnimeDetails(id, getActiveProvider().getProvider());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public AnilistInfoResult getAnimeDetails(final int id, final String provider) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam(PROVIDER_PARAM, provider)
                        .path(ANIME_DETAILS)
                        .build(id)
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleConsumetError)
                .bodyToMono(AnilistInfoResult.class)
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public AnilistInfoResult getAnimeInfo(final int id) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam(PROVIDER_PARAM, getActiveProvider().getProvider())
                        .path(ANIME_DATA)
                        .build(id)
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleConsumetError)
                .bodyToMono(AnilistInfoResult.class)
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public List<AnilistRecommendation> getAnimeRecommendations(final int id) {
        AnilistInfoResult result;
        try {
            result = getAnimeInfo(id);
        } catch (final Exception exception) {
            result = getAnimeDetails(id);
        }

        return isNull(result) || isEmpty(result.getRecommendations())
                ? List.of()
                : result.getRecommendations().stream()
                .filter(recommendation -> nonNull(recommendation.getId()))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public AnilistNextAiringEpisode getNextAiringEpisode(final int id) {
        AnilistInfoResult result;
        try {
            result = getAnimeInfo(id);
        } catch (final Exception exception) {
            result = getAnimeDetails(id);
        }
        return isNull(result) ? null : result.getNextAiringEpisode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public ResultPage<AnilistOverview> getPopularAnime(final int results, final int page) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(POPULAR_ANIME)
                        .queryParam(PAGE_PARAM, page)
                        .queryParam(PER_PAGE_PARAM, results)
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleConsumetError)
                .bodyToMono(new ParameterizedTypeReference<ResultPage<AnilistOverview>>() {

                })
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public ResultPage<AnilistOverview> getTrendingAnime(final int results, final int page) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(TRENDING_ANIME)
                        .queryParam(PAGE_PARAM, page)
                        .queryParam(PER_PAGE_PARAM, results)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleConsumetError)
                .bodyToMono(new ParameterizedTypeReference<ResultPage<AnilistOverview>>() {

                })
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public ResultPage<AnilistRecentEpisode> getRecentEpisodes(final int results, final int page, final String provider) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(RECENT_EPISODES)
                        .queryParam(PAGE_PARAM, page)
                        .queryParam(PER_PAGE_PARAM, results)
                        .queryParam(PROVIDER_PARAM, provider)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleConsumetError)
                .bodyToMono(new ParameterizedTypeReference<ResultPage<AnilistRecentEpisode>>() {

                })
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public ResultPage<AnilistOverview> getAnimeByGenre(final String genre, final int results, final int page) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ADVANCED_SEARCH)
                        .queryParam(TYPE_PARAM, "ANIME")
                        .queryParam(SORT_PARAM, "[\"SCORE_DESC\",\"UPDATED_AT_DESC\",\"TRENDING_DESC\"]")
                        .queryParam(GENRES_PARAM, "[\"" + genre + "\"]")
                        .queryParam(PAGE_PARAM, page)
                        .queryParam(PER_PAGE_PARAM, results)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleConsumetError)
                .bodyToMono(new ParameterizedTypeReference<ResultPage<AnilistOverview>>() {

                })
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public List<AnilistNewsFeed> getNewsFeed() {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ANIME_NEWS_FEED)
                        .queryParam("topic", NewsGenre.getCommaSeparatedGenres())
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleConsumetError)
                .bodyToMono(new ParameterizedTypeReference<List<AnilistNewsFeed>>() {

                })
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public AnilistNewsPost getNewsPost(final String id) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ANIME_NEWS_DETAILS)
                        .queryParam("id", id)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleConsumetError)
                .bodyToMono(AnilistNewsPost.class)
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public AnilistStreamingLinks getEpisodeLinks(final String episodeId, final String provider) {
        return client.get()
                .uri(uriBuilder -> {
                            final UriBuilder builder = uriBuilder
                                    .path(EPISODE_LINKS)
                                    .queryParam(PROVIDER_PARAM, provider);

                            if (provider.equals(ZORO.getProvider())) {
                                builder.queryParam(SERVER_PARAM, "vidstreaming");
                            }

                            return builder.build(episodeId);
                        }
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleConsumetError)
                .bodyToMono(AnilistStreamingLinks.class)
                .doOnError(onObjectMappingErrorLog())
                .block();
    }

    // ======================== PRIVATE METHODS ========================

    private Mono<Throwable> handleConsumetError(final ClientResponse clientResponse) {
        LOGGER.error("[Consumet API] Received an error response from the server with status code '{}'", clientResponse.statusCode());
        return clientResponse.bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        final ConsumetError errorResponse = objectMapper.readValue(response, ConsumetError.class);
                        if (nonNull(errorResponse)) {
                            LOGGER.error("[Consumet API] Consumet API returned an error: '{}'", errorResponse.getMessage());
                            return Mono.error(new ConsumetAPIException(errorResponse.getMessage()));
                        }
                    } catch (final JsonProcessingException noConsumetError) {
                        LOGGER.error("[Consumet API] Unknown error response from Consumet API: '{}'", response);
                    }
                    return Mono.error(new ConsumetAPIException(response));
                });
    }

    private static Consumer<Throwable> onObjectMappingErrorLog() {
        return throwable -> LOGGER.error("[Consumet API] Error while mapping the results to a domain:\n", throwable.getCause());
    }
}
