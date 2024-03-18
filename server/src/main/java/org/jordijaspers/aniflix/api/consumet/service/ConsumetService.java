package org.jordijaspers.aniflix.api.consumet.service;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.repository.DataNotFoundException;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.constant.Genres;
import org.jordijaspers.aniflix.api.anime.model.mapper.AnimeMapper;
import org.jordijaspers.aniflix.api.consumet.model.anilist.AnilistRecentEpisode;
import org.jordijaspers.aniflix.api.consumet.model.anilist.AnilistSearchResult;
import org.jordijaspers.aniflix.api.consumet.repository.ConsumetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.jordijaspers.aniflix.api.consumet.ConsumetConstants.QueryParams.*;
import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.ANIME_NOT_FOUND_ERROR;
import static org.jordijaspers.aniflix.common.util.StringUtil.toInteger;

@Service
@RequiredArgsConstructor
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class ConsumetService {

    private final ConsumetRepository consumetRepository;

    private final AnimeMapper animeMapper;

    public List<AnilistRecentEpisode> getRecentEpisodes(final int perPage, final int page) {
        return consumetRepository.getRecentEpisodes(perPage, page).getResults();
    }

    public List<Anime> getPopular(final int perPage, final int page) {
        return consumetRepository.getPopularAnime(perPage, page).getResults().stream()
                .map(animeMapper::toAnime)
                .toList();
    }

    public List<Anime> getTrending(final int perPage, final int page) {
        return consumetRepository.getTrendingAnime(perPage, page).getResults().stream()
                .map(animeMapper::toAnime)
                .toList();
    }

    public List<Anime> getByGenre(final Genres genre, final int perPage, final int page) {
        return consumetRepository.getAnimeByGenre(genre.getName(), perPage, page).getResults().stream()
                .map(animeMapper::toAnime)
                .toList();
    }

    public List<Anime> searchAnime(final Map<String, String> filters) {
        return consumetRepository.searchAnime(filters).getResults().stream()
                .map(animeMapper::toAnime)
                .toList();
    }

    public Anime getAnimeDetails(final String title) {
        final Map<String, String> filters = applyDefaultFilters(title, new ConcurrentHashMap<>());
        return consumetRepository.searchAnime(filters).getResults().stream()
                .filter(result -> filterResults(result, title))
                .findFirst()
                .map(AnilistSearchResult::getId)
                .map(Integer::parseInt)
                .map(consumetRepository::getAnimeDetails)
                .map(animeMapper::toAnime)
                .orElseThrow(() -> new DataNotFoundException(ANIME_NOT_FOUND_ERROR));
    }

    public Anime getAnimeDetails(final Integer anilistId) {
        return Optional.of(consumetRepository.getAnimeDetails(anilistId))
                .map(animeMapper::toAnime)
                .orElseThrow(() -> new DataNotFoundException(ANIME_NOT_FOUND_ERROR));
    }

    public static Map<String, String> applyDefaultFilters(final String input, final Map<String, String> filters) {
        final String title = input.replace("-", " ");
        final String regex = "\\s+(M{0,3})(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$";
        final Matcher matcher = Pattern.compile(regex).matcher(input.toUpperCase());

        filters.put(FORMAT_PARAM, "TV");
        filters.put(SORT_PARAM, "[\"POPULARITY_DESC\",\"TITLE_ROMAJI_DESC\"]");
        if (matcher.find()) {
            filters.put(PER_PAGE_PARAM, "1");
            filters.put(PAGE_PARAM, toInteger(matcher.group(0).trim()));
            filters.put(QUERY_PARAM, input.substring(0, input.length() - matcher.group(0).length()).trim().toLowerCase());
        } else if (title.contains("season")) {
            filters.put(QUERY_PARAM, title.substring(0, title.indexOf("season")).trim().toLowerCase());
        } else if (title.contains("part")) {
            filters.put(QUERY_PARAM, title.substring(0, title.indexOf("part")).trim().toLowerCase());
        } else {
            filters.put(QUERY_PARAM, input.toLowerCase());
        }

        return filters;
    }

    private boolean filterResults(final AnilistSearchResult result, final String title) {
        final String newTitle = result.getTitle().getRomaji().toLowerCase();
        final String originalTitle = title.toLowerCase();
        if (originalTitle.contains("season")) {
            return newTitle.contains("season") && newTitle.contains(originalTitle.substring(originalTitle.indexOf("season") + 6).trim());
        } else if (originalTitle.contains("part")) {
            return newTitle.contains("part") && newTitle.contains(originalTitle.substring(originalTitle.indexOf("part") + 4).trim());
        } else {
            return true;
        }
    }
}
