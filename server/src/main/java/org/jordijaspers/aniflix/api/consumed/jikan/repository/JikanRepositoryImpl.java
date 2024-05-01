package org.jordijaspers.aniflix.api.consumed.jikan.repository;

import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanException;
import net.sandrohc.jikan.model.anime.Anime;
import net.sandrohc.jikan.model.anime.AnimeEpisode;
import org.jordijaspers.aniflix.common.exception.JikanAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

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
    @Cacheable(value = "jikanAnimeEpisodes", key = "#id")
    public Map<Integer, AnimeEpisode> getAnimeEpisodes(final int id) {
        LOGGER.debug("Fetching anime episodes of the JIKAN API for MAL id: '{}'", id);
        try {
            final List<AnimeEpisode> response = client.query().anime()
                    .episodes(id)
                    .execute()
                    .collectList()
                    .blockOptional()
                    .orElse(new ArrayList<>());
            LOGGER.debug("[Jikan API] Found '{}' episodes: '{}'", response.size(), response);
            return response.stream().collect(toMap(AnimeEpisode::getMalId, Function.identity()));
        } catch (final JikanException exception) {
            LOGGER.error("[Jikan API] Failed to get anime episodes for MAL id: '{}'", id, exception);
            throw new JikanAPIException(exception);
        }
    }
}
