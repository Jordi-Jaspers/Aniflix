package org.jordijaspers.aniflix.api.anime.service;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.repository.DataNotFoundException;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.anime.repository.AnimeRepository;
import org.jordijaspers.aniflix.api.anime.repository.EpisodeRepository;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistNewsPost;
import org.jordijaspers.aniflix.api.consumed.consumet.repository.ConsumetRepository;
import org.jordijaspers.aniflix.api.consumed.consumet.service.ConsumetService;
import org.jordijaspers.aniflix.api.news.model.NewsGenre;
import org.jordijaspers.aniflix.api.news.model.NewsPost;
import org.jordijaspers.aniflix.api.news.repository.NewsRepository;
import org.jordijaspers.aniflix.common.exception.SynchronizationException;
import org.jordijaspers.aniflix.common.util.logging.LogExecutionTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders.GOGOANIME;
import static org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders.ZORO;
import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.ANIME_NOT_FOUND_ERROR;

/**
 * The service which synchronizes certain data from the consumet API with the database.
 */
@Service
@RequiredArgsConstructor
public class SynchronizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizationService.class);

    private final ConsumetService consumetService;

    private final ConsumetRepository consumetRepository;

    private final AnimeRepository animeRepository;

    private final EpisodeRepository episodeRepository;

    private final NewsRepository newsRepository;

    /**
     * Synchronize the news feed data with the database. This will retrieve all the news posts and necessary information.
     */
    @LogExecutionTime
    @Scheduled(fixedDelay = 5, timeUnit = MINUTES)
    public void synchronizeNewsFeed() {
        LOGGER.info("Synchronizing news feed data with the database");
        final LocalDateTime lastUploadedAt = newsRepository.findLatestUploadedAt().orElse(LocalDateTime.MIN);
        final List<NewsPost> posts = consumetRepository.getNewsFeed()
                .stream()
                .filter(anilistFeed -> anilistFeed.getUploadedAt().isAfter(lastUploadedAt))
                .map(anilistFeed -> {
                    final AnilistNewsPost anilistPost = consumetRepository.getNewsPost(anilistFeed.getId());
                    final NewsPost newsPost = new NewsPost();
                    newsPost.setArticleId(anilistPost.getId());
                    newsPost.setTitle(anilistPost.getTitle());
                    newsPost.setUploadedAt(anilistFeed.getUploadedAt());
                    newsPost.setIntro(anilistPost.getIntro());
                    newsPost.setDescription(anilistPost.getDescription());
                    newsPost.setThumbnail(anilistPost.getThumbnail());
                    newsPost.setUrl(anilistPost.getUrl());

                    final NewsGenre topic = anilistFeed.getTopics().stream()
                            .map(NewsGenre::ofName)
                            .findFirst()
                            .orElse(NewsGenre.GENERAL);
                    newsPost.setTopic(topic);

                    return newsPost;
                })
                .toList();

        LOGGER.info("Saving '{}' new news posts", posts.size());
        newsRepository.saveAll(posts);
        LOGGER.info("Synchronization completed for news feed data");
    }

    /**
     * Synchronize the data of the given anime with the database. This will retrieve all the episodes and necessary information.
     *
     * @param anime The anime to synchronize.
     */
    @Async
    @Transactional
    @LogExecutionTime
    public void synchronizeData(final Integer anilistId) {
        final Anime anime = animeRepository.findDetailsByAnilistId(anilistId).orElse(null);
        if (isNull(anime) || anime.isRecentlyUpdated() || anime.isCompleted()) {
            LOGGER.info("Anime with id '{}' is already completed or null, skipping synchronization", anime.getAnilistId());
            return;
        }

        // Save the flag to prevent multiple synchronization jobs on the same anime
        synchronized (this) {
            if (anime.isSynchronizing()) {
                LOGGER.info("Anime with id '{}' is already synchronizing, skipping synchronization", anime.getAnilistId());
                return;
            }
            updateSynchronizationFlag(anime.getAnilistId(), true);
        }

        try {
            // Retrieve the updated anime data from all the providers.
            LOGGER.info("Synchronizing consumet data with the database for id '{}'", anime.getAnilistId());
            final Anime gogoAnimeInfo = consumetService.getAnimeDetailsForProvider(anime.getAnilistId(), GOGOANIME.getProvider());
            final Anime zoroInfo = consumetService.getAnimeDetailsForProvider(anime.getAnilistId(), ZORO.getProvider());

            // Retrieve all the updated episodes data from all the providers.
            final Set<Episode> gogoAnimeEpisodes = gogoAnimeInfo.getEpisodes();
            final Set<Episode> zoroEpisodes = zoroInfo.getEpisodes();

            // Update the incomplete episodes with the new episodes
            updateIncompleteEpisodes(gogoAnimeEpisodes, zoroEpisodes, anime.getEpisodes());

            // Save the non-existing episodes.
            updateNewEpisodes(gogoAnimeEpisodes, zoroEpisodes, anime);

            // Update the anime with the updated data
            gogoAnimeInfo.setGenres(anime.getGenres());
            gogoAnimeInfo.setEpisodes(episodeRepository.findAllByAnilistId(anime.getAnilistId()));

            // Save the updated anime with episode IDs transferred
            gogoAnimeInfo.setUpdated(LocalDateTime.now());
            animeRepository.save(gogoAnimeInfo);
            LOGGER.info("Synchronization completed for anime with id '{}'", anime.getAnilistId());
        } catch (final Exception exception) {
            throw new SynchronizationException(exception);
        } finally {
            synchronized (this) {
                // Ensure the flag is reset even if an error occurs
                updateSynchronizationFlag(anime.getAnilistId(), false);
            }
        }
    }

    /**
     * Update the synchronization flag of the anime.
     *
     * @param anilistId       The Anilist ID of the anime.
     * @param isSynchronizing The new synchronization flag.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void updateSynchronizationFlag(final int anilistId, final boolean isSynchronizing) {
        final Anime anime = animeRepository.findById(anilistId).orElseThrow(() -> new DataNotFoundException(ANIME_NOT_FOUND_ERROR));
        anime.setSynchronizing(isSynchronizing);
        animeRepository.save(anime);
    }

    private void updateNewEpisodes(final Set<Episode> gogoAnimeEpisodes, final Set<Episode> zoroEpisodes, final Anime anime) {
        final Set<Episode> episodes = anime.getEpisodes();

        // Remove the episodes that are already in the database.
        final List<Episode> newEpisodes = gogoAnimeEpisodes.stream()
                .filter(gogoanimeEpisode -> episodes.stream().noneMatch(episode -> episode.getNumber() == gogoanimeEpisode.getNumber()))
                .toList();

        // Update the Zoro ID for the new episodes.
        newEpisodes.forEach(newEpisode -> {
            newEpisode.setAnime(anime);
            final Episode zoroEpisode = zoroEpisodes.stream()
                    .filter(episode -> episode.getNumber() == newEpisode.getNumber())
                    .findFirst()
                    .orElse(null);

            if (nonNull(zoroEpisode)) {
                newEpisode.setZoroId(zoroEpisode.getZoroId());
            }
        });

        // Save the new episodes.
        episodeRepository.saveAll(newEpisodes);
    }

    private void updateIncompleteEpisodes(final Set<Episode> gogoAnimeEpisodes, final Set<Episode> zoroEpisodes,
                                          final Set<Episode> animeEpisodes) {
        final Set<Episode> incompleteEpisodes = animeEpisodes.stream()
                .filter(episode -> isNull(episode.getGogoanimeId()) || isNull(episode.getZoroId()))
                .collect(Collectors.toSet());

        incompleteEpisodes.forEach(incompleteEpisode -> {
            if (isNull(incompleteEpisode.getGogoanimeId())) {
                gogoAnimeEpisodes.stream()
                        .filter(episode -> episode.getNumber() == incompleteEpisode.getNumber())
                        .findFirst()
                        .ifPresent(episode -> {
                            incompleteEpisode.setGogoanimeId(episode.getGogoanimeId());
                            gogoAnimeEpisodes.remove(episode);
                        });
            }


            if (isNull(incompleteEpisode.getZoroId())) {
                zoroEpisodes.stream()
                        .filter(episode -> episode.getNumber() == incompleteEpisode.getNumber())
                        .findFirst()
                        .ifPresent(episode -> {
                            incompleteEpisode.setZoroId(episode.getZoroId());
                            zoroEpisodes.remove(episode);
                        });
            }
        });
        episodeRepository.saveAll(incompleteEpisodes);
    }
}
