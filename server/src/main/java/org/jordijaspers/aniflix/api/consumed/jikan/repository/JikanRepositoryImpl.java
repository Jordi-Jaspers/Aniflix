package org.jordijaspers.aniflix.api.consumed.jikan.repository;

import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanException;
import net.sandrohc.jikan.model.anime.Anime;
import net.sandrohc.jikan.model.anime.AnimeEpisode;
import net.sandrohc.jikan.model.common.RecommendationSimple;
import net.sandrohc.jikan.model.common.Trailer;
import org.jordijaspers.aniflix.common.exception.JikanAPIException;
import org.jordijaspers.aniflix.common.util.logging.LogExecutionTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * The implementation of the {@link JikanRepository} interface.
 */
@Repository
public class JikanRepositoryImpl implements JikanRepository {

    /**
     * The default class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JikanRepository.class);

    /**
     * The client for the Jikan API Wrapper.
     */
    private final Jikan client;

    /**
     * The default all-args constructor.
     */
    public JikanRepositoryImpl() {
        this.client = new Jikan();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    @Cacheable(value = "jikanAnimeDetails", key = "#id")
    public Anime getAnimeDetails(final int id) {
        LOGGER.debug("Fetching anime details of the JIKAN API for MAL id: '{}'", id);
        try {
            return client.query().anime()
                    .get(id)
                    .execute()
                    .blockOptional()
                    .orElseThrow(() -> {
                        LOGGER.error("[Jikan API] Anime with MAL id '{}' does not exist", id);
                        return new JikanAPIException("Anime with MAL id: '" + id + "' not found");
                    });
        } catch (final JikanException exception) {
            LOGGER.error("[Jikan API] Failed to get anime details for MAL id: '{}'", id, exception);
            throw new JikanAPIException(exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    @Cacheable(value = "jikanAnimeTrailer", key = "#id")
    public Trailer getAnimeTrailer(final int id) {
        LOGGER.debug("Fetching anime trailer of the JIKAN API for MAL id: '{}'", id);
        try {
            return client.query().anime()
                    .get(id)
                    .execute()
                    .blockOptional()
                    .orElseThrow(() -> new JikanAPIException("Anime with MAL id: '" + id + "' not found"))
                    .getTrailer();
        } catch (final JikanException exception) {
            LOGGER.error("[Jikan API] Failed to get anime trailer for MAL id: '{}'", id, exception);
            throw new JikanAPIException(exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    @Cacheable(value = "jikanAnimeRecommendations", key = "#id")
    public List<RecommendationSimple> getAnimeRecommendations(final int id) {
        LOGGER.debug("Fetching anime recommendations of the JIKAN API for MAL id: '{}'", id);
        try {
            final List<RecommendationSimple> recommendations = client.query().anime()
                    .recommendations(id)
                    .execute()
                    .collectList()
                    .blockOptional()
                    .orElse(new ArrayList<>());
            LOGGER.debug("[Jikan API] Found '{}' recommendations: '{}'", recommendations.size(), recommendations);
            return recommendations;
        } catch (final JikanException exception) {
            LOGGER.error("[Jikan API] Failed to get anime recommendations for MAL id: '{}'", id, exception);
            throw new JikanAPIException(exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    @Cacheable(value = "jikanAnimeEpisodes", key = "#id")
    public Map<Integer, AnimeEpisode> getAnimeEpisodes(final int id, final int totalEpisodes) {
        LOGGER.debug("Fetching anime episodes of the JIKAN API for MAL id: '{}'", id);
        try {
            final List<AnimeEpisode> episodes = new ArrayList<>();
            for (int i = 1; i <= (totalEpisodes / 100) + 1; i++) {
                final List<AnimeEpisode> response = client.query().anime()
                        .episodes(id)
                        .limit(100)
                        .page(i)
                        .execute()
                        .collectList()
                        .blockOptional()
                        .orElse(new ArrayList<>());
                episodes.addAll(response);
            }
            LOGGER.debug("[Jikan API] Found '{}' episodes: '{}'", episodes.size(), episodes);
            return episodes.stream().collect(toMap(AnimeEpisode::getMalId, Function.identity()));
        } catch (final JikanException exception) {
            LOGGER.error("[Jikan API] Failed to get anime episodes for MAL id: '{}'", id, exception);
            throw new JikanAPIException(exception);
        }
    }
}
