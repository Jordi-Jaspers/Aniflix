package org.jordijaspers.aniflix.api.anime.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.anime.repository.AnimeRepository;
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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.nonNull;
import static java.util.concurrent.TimeUnit.MINUTES;

@Service
@RequiredArgsConstructor
public class SynchronizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizationService.class);

    private final ConsumetService consumetService;

    private final ConsumetRepository consumetRepository;

    private final AnimeRepository animeRepository;

    private final NewsRepository newsRepository;

    /**
     * Synchronize the data of the given anime with the database. This will retrieve all the episodes and necessary information.
     *
     * @param anime The anime to synchronize.
     */
    @Async
    @Transactional
    public void synchronizeData(final Anime anime) {
        LOGGER.info("Synchronizing consumet data with the database for id '{}'", anime.getAnilistId());
        final Anime updatedInfo = consumetService.getAnimeDetails(anime.getAnilistId());
        // Set the Genre from the old anime to the updated anime
        updatedInfo.setGenres(anime.getGenres());

        // Transfer episode IDs from the old anime to the updated one
        final Set<Episode> oldEpisodes = anime.getEpisodes();
        final Set<Episode> updatedEpisodes = updatedInfo.getEpisodes();

        // Assuming that episodes are uniquely identified by some identifier, for example, an episode number
        final Map<String, Episode> episodeMap = new ConcurrentHashMap<>();
        oldEpisodes.forEach(episode -> episodeMap.put(episode.getUrl(), episode));

        // Update the IDs in the updated episodes
        updatedEpisodes.forEach(updatedEpisode -> {
            final Episode oldEpisode = episodeMap.get(updatedEpisode.getUrl());
            updatedEpisode.setAnime(updatedInfo);
            if (nonNull(oldEpisode)) {
                updatedEpisode.setId(oldEpisode.getId());
            }
        });

        // Save the updated anime with episode IDs transferred
        animeRepository.save(updatedInfo);
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
}
