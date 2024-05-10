package org.jordijaspers.aniflix.api.anime.service;

import lombok.RequiredArgsConstructor;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders.GOGOANIME;
import static org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders.ZORO;

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
     * Synchronize the data of the given anime with the database. This will retrieve all the episodes and necessary information.
     *
     * @param anime The anime to synchronize.
     */
    @Async
    @Transactional
    public void synchronizeData(final Anime anime) {
        if (isNull(anime) || anime.isCompleted()) {
            LOGGER.info("Anime with id '{}' is already completed or null, skipping synchronization", anime.getAnilistId());
            return;
        }
        LOGGER.info("Synchronizing consumet data with the database for id '{}'", anime.getAnilistId());

        // Retrieve the updated anime data from all the providers.
        final Anime gogoAnimeInfo = consumetService.getAnimeDetailsForProvider(anime.getAnilistId(), GOGOANIME.getProvider());
        final Anime zoroInfo = consumetService.getAnimeDetailsForProvider(anime.getAnilistId(), ZORO.getProvider());

        // Set the Genre from the old anime to the updated anime
        gogoAnimeInfo.setGenres(anime.getGenres());

        // Retrieve all the updated episodes data from all the providers.
        final Set<Episode> gogoAnimeEpisodes = gogoAnimeInfo.getEpisodes();
        final Set<Episode> zoroEpisodes = zoroInfo.getEpisodes();

        // Update the incomplete episodes with the new episodes
        updateIncompleteEpisodes(gogoAnimeEpisodes, zoroEpisodes, anime.getEpisodes());

        // Update the new episodes with the anime ID
        updateNewEpisodes(gogoAnimeEpisodes, zoroEpisodes, anime);

        // Retrieve all the updated episodes.
        final Set<Episode> updatedEpisodes = episodeRepository.findAllByAnime_AnilistId(anime.getAnilistId());

        // Update the anime with the updated episodes
        gogoAnimeInfo.setEpisodes(updatedEpisodes);

        // Save the updated anime with episode IDs transferred
        animeRepository.save(gogoAnimeInfo);
        LOGGER.info("Synchronization completed for anime with id '{}'", anime.getAnilistId());
    }

    /**
     * Synchronize the news feed data with the database. This will retrieve all the news posts and necessary information.
     */
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
                    newsPost.setTopic(NewsGenre.ofName(anilistFeed.getTopics().get(0)));
                    newsPost.setUrl(anilistPost.getUrl());
                    return newsPost;
                })
                .toList();

        LOGGER.info("Saving '{}' new news posts", posts.size());
        newsRepository.saveAll(posts);
        LOGGER.info("Synchronization completed for news feed data");
    }

    private void updateNewEpisodes(final Set<Episode> gogoAnimeEpisodes, final Set<Episode> zoroEpisodes, final Anime anime) {
        gogoAnimeEpisodes.forEach(gogoanimeEpisode -> {
            final Episode episode = zoroEpisodes.stream()
                    .filter(zoroEpisode -> gogoanimeEpisode.getNumber() == zoroEpisode.getNumber())
                    .findFirst()
                    .orElse(null);

            gogoanimeEpisode.setAnime(anime);
            if (nonNull(episode)) {
                gogoanimeEpisode.setZoroId(episode.getZoroId());
            }
        });
        episodeRepository.saveAll(gogoAnimeEpisodes);
    }

    private void updateIncompleteEpisodes(final Set<Episode> gogoAnimeEpisodes, final Set<Episode> zoroEpisodes,
                                          final Set<Episode> animeEpisodes) {
        final Set<Episode> incompleteEpisodes = animeEpisodes.stream()
                .filter(episode -> isNull(episode.getGogoanimeId()) || isNull(episode.getZoroId()))
                .collect(Collectors.toSet());

        incompleteEpisodes.forEach(incompleteEpisode -> {
            updateEpisodeId(incompleteEpisode, gogoAnimeEpisodes, Episode::getGogoanimeId);
            updateEpisodeId(incompleteEpisode, zoroEpisodes, Episode::getZoroId);
        });
        episodeRepository.saveAll(incompleteEpisodes);
    }

    private void updateEpisodeId(final Episode incompleteEpisode, final Set<Episode> episodes,
                                 final Function<Episode, String> getIdFunction) {
        if (isNull(getIdFunction.apply(incompleteEpisode))) {
            episodes.stream()
                    .filter(episode -> episode.getNumber() == incompleteEpisode.getNumber())
                    .findFirst()
                    .ifPresent(episode -> {
                        getIdFunction.apply(incompleteEpisode);
                        episodes.remove(episode);
                    });
        }
    }
}
