package org.jordijaspers.aniflix.api.anime.service;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.repository.DataNotFoundException;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.anime.model.StreamingLinks;
import org.jordijaspers.aniflix.api.anime.repository.EpisodeRepository;
import org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders;
import org.jordijaspers.aniflix.api.consumed.consumet.service.ConsumetService;
import org.jordijaspers.aniflix.api.consumed.consumet.service.DomainHealthChecker;
import org.jordijaspers.aniflix.api.interaction.service.InteractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;
import static org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders.GOGOANIME;
import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.ANIME_EPISODE_NOT_FOUND_ERROR;
import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.STREAMING_LINKS_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
public class EpisodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpisodeService.class);

    private final EpisodeRepository episodeRepository;

    private final InteractionService interactionService;

    private final DomainHealthChecker healthChecker;

    private final ConsumetService consumetService;

    public Episode getEpisodeOfAnime(final int anilistId, final int episodeNumber) {
        LOGGER.info("Retrieving episode '{}' of anime with anilist id '{}'", episodeNumber, anilistId);
        healthChecker.validateAvailability();
        interactionService.getInteractedAnime(anilistId);

        final AnilistProviders provider = healthChecker.getActiveProvider();
        final Episode episode = episodeRepository.findEpisodeByEpisodeAndAnilistId(anilistId, episodeNumber)
                .orElseThrow(() -> new DataNotFoundException(ANIME_EPISODE_NOT_FOUND_ERROR));

        final String episodeId = switch (provider) {
            case GOGOANIME -> episode.getGogoanimeId();
            case ZORO -> episode.getZoroId();
        };

        final StreamingLinks streamingLinks;
        if (nonNull(episodeId)) {
            LOGGER.info("Retrieving streaming links with episode id '{}'", episodeId);
            streamingLinks = consumetService.getStreamingsLinks(episodeId, provider.getProvider());
        } else {
            final String updatedId = consumetService.getAnimeDetailsForProvider(anilistId, provider.getProvider())
                    .getEpisodes()
                    .stream()
                    .filter(ep -> ep.getNumber() == episodeNumber)
                    .findFirst()
                    .map(ep -> switch (provider) {
                            case GOGOANIME -> ep.getGogoanimeId();
                            case ZORO -> ep.getZoroId();
                    })
                    .orElse(null);

            if (nonNull(updatedId)) {
                LOGGER.info("Retrieving streaming links with updated episode id '{}'", updatedId);
                streamingLinks = consumetService.getStreamingsLinks(updatedId, provider.getProvider());
            } else {
                throw new DataNotFoundException(STREAMING_LINKS_NOT_FOUND_ERROR);
            }
        }

        episode.setStreamingLinks(streamingLinks);
        return episode;
    }
}
