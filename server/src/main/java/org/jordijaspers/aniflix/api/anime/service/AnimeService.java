package org.jordijaspers.aniflix.api.anime.service;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.repository.DataNotFoundException;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.anime.model.constant.Genres;
import org.jordijaspers.aniflix.api.anime.repository.AnimeRepository;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistRecentEpisode;
import org.jordijaspers.aniflix.api.consumed.consumet.service.ConsumetService;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.jordijaspers.aniflix.api.interaction.repository.InteractionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.jordijaspers.aniflix.common.util.SecurityUtil.getLoggedInUser;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimeService.class);

    private final AnimeRepository animeRepository;

    private final InteractionRepository interactionRepository;

    private final ConsumetService consumetService;

    private final SynchronizationService synchronizationService;

    public List<Anime> searchAnime(final Map<String, String> filters) {
        return consumetService.searchAnime(filters);
    }

    public List<AnilistRecentEpisode> getAnimeOfRecentEpisodes(final int perPage, final int page) {
        return consumetService.getRecentEpisodes(perPage, page);
    }

    public Anime getAnimeBanner() {
        final List<Anime> animeTrailers = getPopularAnime(50, 1)
                .stream()
                .filter(anime -> isNotBlank(anime.getTrailerUrl()))
                .toList();

        return animeTrailers.get((int) (Math.random() * animeTrailers.size()));
    }

    public List<Anime> getPopularAnime(final int perPage, final int page) {
        final List<Anime> collection = consumetService.getPopular(perPage, page);
        applyUserInteractions(collection);
        return collection;
    }

    public List<Anime> getTrendingAnime(final int perPage, final int page) {
        final List<Anime> collection = consumetService.getTrending(perPage, page);
        applyUserInteractions(collection);
        return collection;
    }

    public List<Anime> getAnimeByGenre(final Genres genre, final int perPage, final int page) {
        final List<Anime> collection = consumetService.getByGenre(genre, perPage, page);
        applyUserInteractions(collection);
        return collection;
    }

    public Anime findByAnilistId(final int anilistId) {
        LOGGER.info("Attempting to look up anime with Anilist ID '{}'", anilistId);
        return animeRepository.findByAnilistId(anilistId)
                .map(this::updateAnimeInfo)
                .orElseGet(() -> saveAnime(consumetService.getAnimeDetails(anilistId)));
    }

    public Anime findAnimeByTitle(final String title) {
        if (isBlank(title)) {
            LOGGER.warn("Cannot look up a blank anime title.");
            return null;
        }

        LOGGER.info("Attempting to look up anime with title '{}'", title);
        return animeRepository.findByTitle(title)
                .map(this::updateAnimeInfo)
                .orElseGet(() -> {
                    try {
                        return saveAnime(consumetService.getAnimeDetails(title));
                    } catch (final DataNotFoundException exception) {
                        LOGGER.warn("Anime with title '{}' not found.", title);
                        return null;
                    }
                });
    }

    private Anime updateAnimeInfo(final Anime anime) {
        if (!anime.isCompleted()) {
            synchronizationService.synchronizeData(anime);
        }
        return anime;
    }

    private Anime saveAnime(final Anime anime) {
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

    private void applyUserInteractions(final List<Anime> collection) {
        final List<Interaction> interactions = interactionRepository.findAllByAnilistIdIn(collection, getLoggedInUser());
        interactions.forEach(interaction -> {
            collection.stream()
                    .filter(anime -> anime.equals(interaction.getAnime()))
                    .findFirst()
                    .ifPresent(anime -> {
                        anime.setWatchStatus(interaction.getWatchStatus());
                        anime.setLiked(interaction.isLiked());
                        anime.setInLibrary(interaction.isInLibrary());
                    });
        });
    }
}
