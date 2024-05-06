package org.jordijaspers.aniflix.api.consumed.consumet.service;

import lombok.RequiredArgsConstructor;
import net.sandrohc.jikan.model.anime.AnimeEpisode;
import org.hawaiiframework.repository.DataNotFoundException;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.StreamingLinks;
import org.jordijaspers.aniflix.api.anime.model.StreamingSource;
import org.jordijaspers.aniflix.api.anime.model.constant.Genres;
import org.jordijaspers.aniflix.api.anime.model.mapper.AnimeMapper;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistRecentEpisode;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistSearchResult;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistSource;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistStreamingLinks;
import org.jordijaspers.aniflix.api.consumed.consumet.repository.ConsumetRepository;
import org.jordijaspers.aniflix.api.consumed.jikan.repository.JikanRepository;
import org.jordijaspers.aniflix.api.recommendation.model.Recommendation;
import org.jordijaspers.aniflix.api.recommendation.model.mapper.RecommendationMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.jordijaspers.aniflix.api.consumed.consumet.ConsumetConstants.QueryParams.*;
import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.ANIME_NOT_FOUND_ERROR;
import static org.jordijaspers.aniflix.common.util.StringUtil.toInteger;

@Service
@RequiredArgsConstructor
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class ConsumetService {

    private final ConsumetRepository consumetRepository;

    private final JikanRepository jikanRepository;

    private final AnimeMapper animeMapper;

    private final RecommendationMapper recommendationMapper;

    @Cacheable(value = "recentEpisodes", unless = "#result.size() == 0")
    public List<AnilistRecentEpisode> getRecentEpisodes(final int perPage, final int page) {
        return consumetRepository.getRecentEpisodes(perPage, page).getResults();
    }

    @Cacheable(value = "popularAnime", unless = "#result.size() == 0")
    public List<Anime> getPopular(final int perPage, final int page) {
        return consumetRepository.getPopularAnime(perPage, page).getResults().stream()
                .map(animeMapper::toAnime)
                .toList();
    }

    @Cacheable(value = "trendingAnime", unless = "#result.size() == 0")
    public List<Anime> getTrending(final int perPage, final int page) {
        return consumetRepository.getTrendingAnime(perPage, page).getResults().stream()
                .map(animeMapper::toAnime)
                .toList();
    }

    @Cacheable(value = "animeDetails", key = "#anilistId")
    public Anime getAnimeDetails(final Integer anilistId) {
        final Anime anime = Optional.of(consumetRepository.getAnimeDetails(anilistId))
                .map(animeMapper::toAnime)
                .orElseThrow(() -> new DataNotFoundException(ANIME_NOT_FOUND_ERROR));
        return provisionDataFromJikan(anime);
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

    @Cacheable(value = "animeRecommendations", key = "#anilistId")
    public List<Recommendation> getRecommendationsForAnime(final int anilistId) {
        return consumetRepository.getAnimeRecommendations(anilistId).stream()
                .map(recommendationMapper::toRecommendation)
                .toList();
    }
    @Cacheable(value = "streamingLinks", key = "#episodeId")
    public StreamingLinks getStreamingsLinks(final String episodeId) {
        final AnilistStreamingLinks anilistLinks = consumetRepository.getEpisodeLinks(episodeId);
        final List<StreamingSource> sources = anilistLinks.getSources().stream()
                .map(source -> new StreamingSource(source.getUrl(), source.getQuality()))
                .toList();

        return new StreamingLinks(anilistLinks.getHeaders().get("Referer"), sources);
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

    // ======================================== PRIVATE METHODS ========================================

    private static Map<String, String> applyDefaultFilters(final String input, final Map<String, String> filters) {
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

    private Anime provisionDataFromJikan(final Anime anime) {
        if (isBlank(anime.getTrailerUrl())) {
            final String trailerUrl = jikanRepository.getAnimeTrailer(anime.getMalId()).getYoutubeId();
            anime.setTrailerUrl(trailerUrl);
        }

        final Map<Integer, AnimeEpisode> jikanEpisodes = jikanRepository.getAnimeEpisodes(anime.getMalId(), anime.getTotalEpisodes());
        anime.getEpisodes().forEach(episode -> {
            final AnimeEpisode jikanEpisode = jikanEpisodes.get(episode.getNumber());
            if (nonNull(jikanEpisode)) {
                final OffsetDateTime airDate = jikanEpisode.getAired();
                if (nonNull(airDate)) {
                    episode.setAirDate(airDate.toLocalDateTime());
                }

                final long duration = nonNull(jikanEpisode.getDuration())
                        ? jikanEpisode.getDuration().toSeconds()
                        : 1440;
                episode.setDuration(duration);

                final String title = nonNull(jikanEpisode.getTitle())
                        ? jikanEpisode.getTitle()
                        : "Episode " + episode.getNumber();
                episode.setTitle(title);
            }
        });

        return anime;
    }
}
