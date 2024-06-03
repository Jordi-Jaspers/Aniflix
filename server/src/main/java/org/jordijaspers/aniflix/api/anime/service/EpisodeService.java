package org.jordijaspers.aniflix.api.anime.service;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.repository.DataNotFoundException;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.anime.model.StreamingLinks;
import org.jordijaspers.aniflix.api.anime.model.constant.AnimeStatus;
import org.jordijaspers.aniflix.api.anime.repository.AnimeRepository;
import org.jordijaspers.aniflix.api.anime.repository.EpisodeRepository;
import org.jordijaspers.aniflix.api.consumed.anizip.repository.AnizipRepository;
import org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders;
import org.jordijaspers.aniflix.api.consumed.consumet.service.ConsumetService;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.jordijaspers.aniflix.api.interaction.service.InteractionService;
import org.jordijaspers.aniflix.api.progress.model.EpisodeProgress;
import org.jordijaspers.aniflix.api.progress.model.request.EpisodeProgressRequest;
import org.jordijaspers.aniflix.api.progress.repository.ProgressRepository;
import org.jordijaspers.aniflix.api.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static org.jordijaspers.aniflix.api.anime.model.constant.AnimeStatus.COMPLETED;
import static org.jordijaspers.aniflix.api.anime.model.constant.AnimeStatus.ONGOING;
import static org.jordijaspers.aniflix.api.consumed.consumet.service.DomainHealthChecker.getActiveProvider;
import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.ANIME_EPISODE_NOT_FOUND_ERROR;
import static org.jordijaspers.aniflix.common.util.SecurityUtil.getLoggedInUser;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * The service which handles the episode data.
 */
@Service
@RequiredArgsConstructor
public class EpisodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpisodeService.class);

    private final EpisodeRepository episodeRepository;

    private final ProgressRepository progressRepository;

    private final AnimeRepository animeRepository;

    private final InteractionService interactionService;

    private final SynchronizationService synchronizationService;

    private final ConsumetService consumetService;

    private final AnizipRepository anizipRepository;

    public Set<Episode> getEpisodesOfAnime(final int anilistId) {
        LOGGER.info("Retrieving episodes of anime with anilist id '{}'", anilistId);
        final Set<Episode> episodes = episodeRepository.findAllByAnilistId(anilistId);
        if (isEmpty(episodes)) {
            LOGGER.debug("Episodes not found in the database, retrieving episodes from API");
            final Anime anime = consumetService.getAnimeDetails(anilistId);
            try {
                animeRepository.save(anime);
            } catch (final Exception exception) {
                LOGGER.error("Could not save anime with anilist id '{}'", anilistId, exception);
            }
            return anime.getEpisodes();
        }


    }

    public Set<Episode> getEpisodeOfAnilistId(final int anilistId) {
        LOGGER.info("Retrieving episodes of anime with anilist id '{}'", anilistId);
        final Set<Episode> episodes = animeRepository.existsByAnilistIdAndStatus(anilistId, COMPLETED)
                ? episodeRepository.findAllByAnilistId(anilistId)
                : anizipRepository.getAnizipInfoByAniListId(anilistId);

        episodes.forEach(episode -> episode.getEpisodeProgresses().stream()
                .filter(episodeProgress -> episodeProgress.getUser().equals(getLoggedInUser()))
                .findFirst()
                .ifPresent(episodeProgress -> episode.setLastSeen(episodeProgress.getLastSeen())));

        return episodes;
    }

    public Episode getEpisodeOfAnime(final int anilistId, final int episodeNumber) {
        LOGGER.info("Retrieving episode '{}' of anime with anilist id '{}'", episodeNumber, anilistId);
        final AnilistProviders provider = getActiveProvider();
        final Episode episode = getInteractedEpisode(anilistId, episodeNumber, provider);
        final StreamingLinks streamingLinks = consumetService.getStreamingsLinks(episode.getActiveEpisodeId(), provider.getProvider());
        episode.setStreamingLinks(streamingLinks);

        final List<EpisodeProgress> episodeProgresses = episode.getEpisodeProgresses();
        episodeProgresses.stream()
                .filter(episodeProgress -> episodeProgress.getUser().equals(getLoggedInUser()))
                .findFirst()
                .ifPresent(episodeProgress -> episode.setLastSeen(episodeProgress.getLastSeen()));

        return episode;
    }

    public void updateProgress(final EpisodeProgressRequest request, final User loggedInUser) {
        LOGGER.info("Updating progress for episode '{}' of anime with anilist id '{}'", request.getEpisode(), request.getAnilistId());
        final Episode episode = episodeRepository.findEpisodeByEpisodeAndAnilistId(request.getAnilistId(), request.getEpisode())
                .orElseThrow(() -> new DataNotFoundException(ANIME_EPISODE_NOT_FOUND_ERROR));

        episode.getEpisodeProgresses().stream()
                .filter(episodeProgress -> episodeProgress.getUser().equals(loggedInUser))
                .findFirst()
                .ifPresentOrElse(
                        episodeProgress -> {
                            episodeProgress.setLastSeen(request.getLastSeen());
                            progressRepository.save(episodeProgress);
                        },
                        () -> {
                            final EpisodeProgress progress = new EpisodeProgress(request.getLastSeen(), episode, loggedInUser);
                            progressRepository.save(progress);
                        });
    }

    private Episode getEpisodeFromAPI(final int anilistId, final int episodeNumber, final AnilistProviders provider) {
        LOGGER.info("Episode not found in database, retrieving episode from API");
        final Anime anime = consumetService.getAnimeDetailsForProvider(anilistId, provider.getProvider());
        final Episode episode = anime.getEpisodes()
                .stream()
                .filter(consumetEpisode -> consumetEpisode.getNumber() == episodeNumber)
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException(ANIME_EPISODE_NOT_FOUND_ERROR));

        try {
            episodeRepository.save(episode);
        } catch (final Exception exception) {
            LOGGER.error("Could not save episode with anilist id '{}' and episode number '{}'", anilistId, episodeNumber, exception);
        }

        synchronizationService.synchronizeData(anilistId);
        return episode;
    }

    private Set<Episode> getEpisodesFromAPI(final int anilistId) {

        synchronizationService.synchronizeData(anilistId);
        return anime.getEpisodes();
    }

    private Episode getInteractedEpisode(final int anilistId, final int episodeNumber, final AnilistProviders provider) {
        interactionService.setLastSeenEpisode(anilistId, episodeNumber);
        return episodeRepository.existsByAnime_AnilistIdAndNumber(anilistId, episodeNumber)
                ? episodeRepository.findEpisodeByEpisodeAndAnilistId(anilistId, episodeNumber).orElse(null)
                : getEpisodeFromAPI(anilistId, episodeNumber, provider);
    }
}
