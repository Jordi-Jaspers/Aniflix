package org.jordijaspers.aniflix.api.anime.service;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.repository.DataNotFoundException;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.anime.model.Overview;
import org.jordijaspers.aniflix.api.anime.repository.AnimeRepository;
import org.jordijaspers.aniflix.api.consumet.model.gogoanime.GogoAnimeEpisode;
import org.jordijaspers.aniflix.api.consumet.service.ConsumetService;
import org.jordijaspers.aniflix.api.genre.model.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hawaiiframework.async.AsyncUtil.invoke;
import static org.jordijaspers.aniflix.api.anime.model.constant.Genres.getRandomGenres;
import static org.jordijaspers.aniflix.api.consumet.ConsumetConstants.QueryParams.*;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimeService.class);

    private final ConsumetService consumetService;

    private final AnimeRepository animeRepository;

    public Overview getOverviewPage() {
        final CompletableFuture<List<Anime>> popularFuture = fetchPopularAnimeAsync();
        final CompletableFuture<List<Anime>> trendingFuture = fetchTrendingAnimeAsync();
        final CompletableFuture<List<Anime>> recentFuture = fetchRecentAnimeAsync();
        final Map<Genre, CompletableFuture<List<Anime>>> genreFutures = fetchGenreAnimeAsync();

        final CompletableFuture<Void> allOf = CompletableFuture.allOf(popularFuture, trendingFuture, recentFuture);
        allOf.join();

        return Overview.builder()
                .popular(popularFuture.join())
                .trending(trendingFuture.join())
                .recentlyAdded(recentFuture.join())
                .genre(genreFutures.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().join()
                        )))
                .build();
    }

    public List<Anime> searchAnime(final Map<String, String> filters) {
        return consumetService.searchAnime(filters);
    }

    public List<Anime> getAnimeOfRecentEpisodes(final int perPage, final int page) {
        return consumetService.getRecentEpisodes(perPage, page).stream()
                .map(GogoAnimeEpisode::getTitle)
                .map(this::findAnimeByTitle)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    public List<Anime> getPopularAnime(final int perPage, final int page) {
        return consumetService.getPopular(perPage, page);
    }

    public List<Anime> getTrendingAnime(final int perPage, final int page) {
        return consumetService.getTrending(perPage, page);
    }

    public List<Anime> getAnimeByGenre(final String genre, final int perPage, final int page) {
        return consumetService.getByGenre(genre, perPage, page);
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

    public Anime findByAnilistId(final int anilistId) {
        LOGGER.info("Attempting to look up anime with Anilist ID '{}'", anilistId);
        return animeRepository.findByAnilistId(anilistId)
                .map(this::updateAnimeInfo)
                .orElseGet(() -> saveAnime(consumetService.getAnimeDetails(anilistId)));
    }

    public Anime saveAnime(final Anime anime) {
        LOGGER.info("Anime with title '{}' not yet in the database, attempting to save it.", anime.getTitle());

        // Detach episodes from the anime temporarily
        final Set<Episode> episodes = anime.getEpisodes();
        anime.setEpisodes(new HashSet<>());

        // Save the anime without episodes first
        final Anime preSave = animeRepository.save(anime);

        // Reattach episodes and set their anime reference
        episodes.forEach(episode -> {
            episode.setAnime(preSave);
            preSave.getEpisodes().add(episode);
        });

        // Save the anime with episodes
        return animeRepository.save(preSave);
    }

    public Anime updateAnimeInfo(final Anime anime) {
        if (anime.isCompleted()) {
            return anime;
        }

        LOGGER.info("Anime with title '{}' is incomplete, attempting to update it.", anime.getTitle());
        final Anime updatedAnime = consumetService.getAnimeDetails(anime.getAnilistId());

        // Set the Genre from the old anime to the updated anime
        updatedAnime.setGenres(anime.getGenres());

        // Transfer episode IDs from the old anime to the updated one
        final Set<Episode> oldEpisodes = anime.getEpisodes();
        final Set<Episode> updatedEpisodes = updatedAnime.getEpisodes();

        // Assuming that episodes are uniquely identified by some identifier, for example, an episode number
        final Map<String, Episode> episodeMap = new ConcurrentHashMap<>();
        oldEpisodes.forEach(episode -> episodeMap.put(episode.getUrlId(), episode));

        // Update the IDs in the updated episodes
        updatedEpisodes.forEach(updatedEpisode -> {
            final Episode oldEpisode = episodeMap.get(updatedEpisode.getUrlId());
            updatedEpisode.setAnime(updatedAnime);
            if (nonNull(oldEpisode)) {
                updatedEpisode.setId(oldEpisode.getId());
            }
        });

        // Save the updated anime with episode IDs transferred
        return animeRepository.save(updatedAnime);
    }

    private CompletableFuture<List<Anime>> fetchPopularAnimeAsync() {
        return invoke(() -> getPopularAnime(PER_PAGE_VALUE, PAGE_VALUE));
    }

    private CompletableFuture<List<Anime>> fetchTrendingAnimeAsync() {
        return invoke(() -> getTrendingAnime(PER_PAGE_VALUE, PAGE_VALUE));
    }

    private CompletableFuture<List<Anime>> fetchRecentAnimeAsync() {
        return invoke(() -> getAnimeOfRecentEpisodes(PER_PAGE_VALUE_RECENT, PAGE_VALUE));
    }

    private Map<Genre, CompletableFuture<List<Anime>>> fetchGenreAnimeAsync() {
        return getRandomGenres(4).stream()
                .collect(Collectors.toMap(
                        genre -> genre,
                        genre -> invoke(() -> getAnimeByGenre(genre.getNameAsString(), PER_PAGE_VALUE, PAGE_VALUE))
                ));
    }
}
