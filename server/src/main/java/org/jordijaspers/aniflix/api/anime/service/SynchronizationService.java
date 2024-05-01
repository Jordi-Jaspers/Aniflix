package org.jordijaspers.aniflix.api.anime.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.anime.repository.AnimeRepository;
import org.jordijaspers.aniflix.api.consumed.consumet.service.ConsumetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class SynchronizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizationService.class);

    private final ConsumetService consumetService;

    private final AnimeRepository animeRepository;

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
}
