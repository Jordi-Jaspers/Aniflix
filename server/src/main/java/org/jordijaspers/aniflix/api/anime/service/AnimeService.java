package org.jordijaspers.aniflix.api.anime.service;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.repository.DataNotFoundException;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.anime.model.constant.Genres;
import org.jordijaspers.aniflix.api.anime.repository.AnimeRepository;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistRecentEpisode;
import org.jordijaspers.aniflix.api.consumed.consumet.service.ConsumetService;
import org.jordijaspers.aniflix.api.interaction.service.UserInteractionEnhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.jordijaspers.aniflix.api.anime.model.constant.AnimeStatus.COMPLETED;

/**
 * The service which handles the anime data.
 */
@Service
@RequiredArgsConstructor
public class AnimeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimeService.class);

    private final AnimeRepository animeRepository;

    private final UserInteractionEnhancer userInteractionEnhancer;

    private final ConsumetService consumetService;

    private final SynchronizationService synchronizationService;

    public Anime getAnimeBanner() {
        final List<Anime> animeTrailers = getPopularAnime(50, 1)
                .stream()
                .filter(anime -> isNotBlank(anime.getTrailerUrl()))
                .toList();

        return animeTrailers.get((int) (Math.random() * animeTrailers.size()));
    }

    public List<Anime> searchAnime(final Map<String, String> filters) {
        final List<Anime> anime = consumetService.searchAnime(filters);
        userInteractionEnhancer.applyAnime(anime);
        return anime;
    }

    public List<AnilistRecentEpisode> getAnimeOfRecentEpisodes(final int perPage, final int page) {
        return consumetService.getRecentEpisodes(perPage, page);
    }

    public Page<Anime> getPopularAnime(final int perPage, final int page) {
        final Page<Anime> anime = consumetService.getPopular(perPage, page);
        userInteractionEnhancer.applyAnime(anime);
        return anime;
    }

    public Page<Anime> getTrendingAnime(final int perPage, final int page) {
        final Page<Anime> anime = consumetService.getTrending(perPage, page);
        userInteractionEnhancer.applyAnime(anime);
        return anime;
    }

    public Page<Anime> getAnimeByGenre(final Genres genre, final int perPage, final int page) {
        final Page<Anime> anime = consumetService.getByGenre(genre, perPage, page);
        userInteractionEnhancer.applyAnime(anime);
        return anime;
    }

    public Anime findInfoByAnilistId(final int anilistId) {
        LOGGER.info("Attempting to look up anime info with Anilist ID '{}'", anilistId);
        final Anime anime = animeRepository.findInfoByAnilistId(anilistId)
                .orElseGet(() -> saveAnime(consumetService.getAnimeInfo(anilistId)));

        userInteractionEnhancer.applyAnime(anime);
        synchronizationService.synchronizeData(anilistId);
        return anime;
    }

    public Anime findAnimeByTitle(final String title) {
        if (isBlank(title)) {
            LOGGER.warn("Cannot look up a blank anime title.");
            return null;
        }

        LOGGER.info("Attempting to look up anime with title '{}'", title);
        final Anime anime = animeRepository.findByTitle(title)
                .orElseGet(() -> {
                    try {
                        return saveAnime(consumetService.getAnimeDetails(title));
                    } catch (final DataNotFoundException exception) {
                        LOGGER.warn("Anime with title '{}' not found.", title);
                        return null;
                    }
                });

        userInteractionEnhancer.applyAnime(anime);
        return anime;
    }

    public Anime saveAnime(final Anime anime) {
        LOGGER.info("Anime with title '{}' not yet in the database, attempting to save it.", anime.getTitle());

        // Detach episodes from the anime temporarily
        final Set<Episode> episodes = anime.getEpisodes();
        anime.setEpisodes(new HashSet<>());

        // Save the anime without episodes first
        final Anime preSave = animeRepository.save(anime);

        // Reattach episodes and set their anime reference
        episodes.forEach(episode -> episode.setAnime(preSave));
        preSave.setEpisodes(episodes);

        // Save the anime with episodes
        return animeRepository.save(preSave);
    }

    public boolean isAnimeInDatabase(final int anilistId) {
        return animeRepository.existsById(anilistId);
    }

    public boolean isAnimeStatusCompleted(final int anilistId) {
        return animeRepository.existsByAnilistIdAndStatus(anilistId, COMPLETED);
    }
}
