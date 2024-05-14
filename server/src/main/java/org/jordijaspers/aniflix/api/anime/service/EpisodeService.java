package org.jordijaspers.aniflix.api.anime.service;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.repository.DataNotFoundException;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.anime.model.StreamingLinks;
import org.jordijaspers.aniflix.api.anime.repository.EpisodeRepository;
import org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders;
import org.jordijaspers.aniflix.api.consumed.consumet.service.ConsumetService;
import org.jordijaspers.aniflix.api.interaction.service.InteractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.nonNull;
import static org.jordijaspers.aniflix.api.consumed.consumet.service.DomainHealthChecker.getActiveProvider;
import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.STREAMING_LINKS_NOT_FOUND_ERROR;

/**
 * The service which handles the episode data.
 */
@Service
@RequiredArgsConstructor
public class EpisodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpisodeService.class);

    private final EpisodeRepository episodeRepository;

    private final InteractionService interactionService;

    private final ConsumetService consumetService;

    public Episode getEpisodeOfAnime(final int anilistId, final int episodeNumber) {
        LOGGER.info("Retrieving episode '{}' of anime with anilist id '{}'", episodeNumber, anilistId);
        final AnilistProviders provider = getActiveProvider();
        final Episode episode = getInteractedEpisode(anilistId, episodeNumber);
        final String episodeId = episode.getActiveEpisodeId();
        final StreamingLinks streamingLinks = nonNull(episodeId)
                ? consumetService.getStreamingsLinks(episodeId, provider.getProvider())
                : getEpisodeIdFromAPI(anilistId, episodeNumber, provider)
                .map(id -> consumetService.getStreamingsLinks(id, provider.getProvider()))
                .orElseGet(() -> {
                    LOGGER.error("Episode id not found for episode '{}' of anime with anilist id '{}'", episodeNumber, anilistId);
                    throw new DataNotFoundException(STREAMING_LINKS_NOT_FOUND_ERROR);
                });

        episode.setStreamingLinks(streamingLinks);
        return episode;
    }

    private Optional<String> getEpisodeIdFromAPI(final int anilistId, final int episodeNumber, final AnilistProviders provider) {
        LOGGER.info("Episode not found in database, retrieving episode id from API");
        return consumetService.getAnimeDetailsForProvider(anilistId, provider.getProvider())
                .getEpisodes()
                .stream()
                .filter(consumetEpisode -> consumetEpisode.getNumber() == episodeNumber)
                .findFirst()
                .map(Episode::getActiveEpisodeId);
    }

    private Episode getInteractedEpisode(final int anilistId, final int episodeNumber) {
        interactionService.getInteractedAnime(anilistId);
        return episodeRepository.findEpisodeByEpisodeAndAnilistId(anilistId, episodeNumber).orElse(null);
    }
}
