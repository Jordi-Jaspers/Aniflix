package org.jordijaspers.aniflix.api.anime.service;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.repository.DataNotFoundException;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.anime.model.StreamingLinks;
import org.jordijaspers.aniflix.api.anime.repository.EpisodeRepository;
import org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders;
import org.jordijaspers.aniflix.api.consumed.consumet.service.ConsumetService;
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

import static org.jordijaspers.aniflix.api.consumed.consumet.service.DomainHealthChecker.getActiveProvider;
import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.ANIME_EPISODE_NOT_FOUND_ERROR;
import static org.jordijaspers.aniflix.common.util.SecurityUtil.getLoggedInUser;

/**
 * The service which handles the episode data.
 */
@Service
@RequiredArgsConstructor
public class EpisodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpisodeService.class);

    private final EpisodeRepository episodeRepository;

    private final ProgressRepository progressRepository;

    private final InteractionService interactionService;

    private final AnimeService animeService;

    private final SynchronizationService synchronizationService;

    private final ConsumetService consumetService;

    public Episode getInteractedEpisode(final int anilistId, final int episodeNumber, final AnilistProviders provider) {
        interactionService.setLastSeenEpisode(anilistId, episodeNumber);
        return episodeRepository.existsByAnime_AnilistIdAndNumber(anilistId, episodeNumber)
                ? episodeRepository.findEpisodeByEpisodeAndAnilistId(anilistId, episodeNumber).orElse(null)
                : getEpisodeFromAPI(anilistId, episodeNumber, provider);
    }

    public Set<Episode> getEpisodesOfAnime(final int anilistId) {
        LOGGER.info("Retrieving episodes of anime with anilist id '{}'", anilistId);
        final Set<Episode> episodes = animeService.isAnimeStatusCompleted(anilistId)
                ? episodeRepository.findAllByAnilistId(anilistId)
                : getEpisodesFromApi(anilistId);

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
        LOGGER.debug("Updating progress for episode '{}' of anime with anilist id '{}'", request.getEpisode(), request.getAnilistId());
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

    private Set<Episode> getEpisodesFromApi(final int anilistId) {
        final Anime anime = consumetService.getAnimeInfo(anilistId);
        if (!animeService.isAnimeInDatabase(anilistId)) {
            animeService.saveAnime(anime);
        }

        synchronizationService.synchronizeData(anilistId);
        return anime.getEpisodes();
    }

    private Set<Episode> getEpisodesFromApi(final int anilistId, final AnilistProviders provider) {
        final Anime anime = consumetService.getAnimeDetailsForProvider(anilistId, provider.getProvider());
        if (!animeService.isAnimeInDatabase(anilistId)) {
            animeService.saveAnime(anime);
        }

        synchronizationService.synchronizeData(anilistId);
        return anime.getEpisodes();
    }

    private Episode getEpisodeFromAPI(final int anilistId, final int episodeNumber, final AnilistProviders provider) {
        LOGGER.info("Episode not found in database, retrieving episode from API");
        final Set<Episode> episodes = getEpisodesFromApi(anilistId, provider);
        return episodes.stream()
                .filter(consumetEpisode -> consumetEpisode.getNumber() == episodeNumber)
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException(ANIME_EPISODE_NOT_FOUND_ERROR));
    }
}
