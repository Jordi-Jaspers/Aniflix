package org.jordijaspers.aniflix.api.consumed.consumet.service;

import lombok.RequiredArgsConstructor;
import net.sandrohc.jikan.model.anime.AnimeEpisode;
import org.hawaiiframework.repository.DataNotFoundException;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.StreamingLinks;
import org.jordijaspers.aniflix.api.anime.model.StreamingSource;
import org.jordijaspers.aniflix.api.anime.model.constant.Genres;
import org.jordijaspers.aniflix.api.anime.model.mapper.AnimeMapper;
import org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders;
import org.jordijaspers.aniflix.api.consumed.consumet.model.ResultPage;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistNextAiringEpisode;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistOverview;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistRecentEpisode;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistSearchResult;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistStreamingLinks;
import org.jordijaspers.aniflix.api.consumed.consumet.repository.ConsumetRepository;
import org.jordijaspers.aniflix.api.consumed.jikan.repository.JikanRepository;
import org.jordijaspers.aniflix.api.recommendation.model.Recommendation;
import org.jordijaspers.aniflix.api.recommendation.model.mapper.RecommendationMapper;
import org.jordijaspers.aniflix.api.schedule.model.NextAiringEpisode;
import org.jordijaspers.aniflix.api.schedule.model.mapper.ScheduleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.jordijaspers.aniflix.api.consumed.consumet.ConsumetConstants.QueryParams.*;
import static org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders.getProviderByProvider;
import static org.jordijaspers.aniflix.api.consumed.consumet.service.DomainHealthChecker.getActiveProvider;
import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.ANIME_NOT_FOUND_ERROR;
import static org.jordijaspers.aniflix.common.util.StringUtil.toInteger;

/**
 * The ConsumetService is responsible for fetching data from the Consumet API.
 */
@Service
@RequiredArgsConstructor
public class ConsumetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumetService.class);

    private final ConsumetRepository consumetRepository;

    private final JikanRepository jikanRepository;

    private final AnimeMapper animeMapper;

    private final RecommendationMapper recommendationMapper;

    private final ScheduleMapper scheduleMapper;

    public List<AnilistRecentEpisode> getRecentEpisodes(final int perPage, final int page) {
        LOGGER.info("[Consumet API] Fetching recent episodes from Anilist.");
        final AnilistProviders provider = getActiveProvider();
        return consumetRepository.getRecentEpisodes(perPage, page, provider.getProvider()).getResults();
    }

    @Cacheable(value = "popularAnime", key = "#perPage + #page", unless = "#result.getTotalElements() == 0")
    public Page<Anime> getPopular(final int perPage, final int page) {
        LOGGER.info("[Consumet API] Fetching popular anime from Anilist.");
        final ResultPage<AnilistOverview> popularAnime = consumetRepository.getPopularAnime(perPage, page);
        return animeMapper.toOverviewPage(popularAnime);
    }

    @Cacheable(value = "trendingAnime", key = "#perPage + #page", unless = "#result.getTotalElements() == 0")
    public Page<Anime> getTrending(final int perPage, final int page) {
        LOGGER.info("[Consumet API] Fetching trending anime from Anilist.");
        final ResultPage<AnilistOverview> trendingAnime = consumetRepository.getTrendingAnime(perPage, page);
        return animeMapper.toOverviewPage(trendingAnime);
    }

    @Cacheable(value = "animeByGenre", key = "#genre.getName() + #perPage + #page", unless = "#result.getTotalElements() == 0")
    public Page<Anime> getByGenre(final Genres genre, final int perPage, final int page) {
        LOGGER.info("[Consumet API] Fetching anime by genre '{}' from Anilist.", genre.getName());
        final ResultPage<AnilistOverview> animeByGenre = consumetRepository.getAnimeByGenre(genre.getName(), perPage, page);
        return animeMapper.toOverviewPage(animeByGenre);
    }

    public Anime getAnimeDetails(final Integer anilistId) {
        LOGGER.info("[Consumet API] Fetching anime details for Anilist ID '{}'.", anilistId);
        final Anime anime = Optional.of(consumetRepository.getAnimeDetails(anilistId))
                .map(animeMapper::toDomainObject)
                .orElseThrow(() -> new DataNotFoundException(ANIME_NOT_FOUND_ERROR));

        anime.getEpisodes().forEach(episode -> {
            episode.setActiveEpisodeId(getActiveProvider());
            episode.setAnime(anime);
        });
        return provisionDataFromJikan(anime);
    }

    public Anime getAnimeDetails(final String title) {
        LOGGER.info("[Consumet API] Fetching anime details for title '{}'.", title);
        final Map<String, String> filters = applyDefaultFilters(title, new ConcurrentHashMap<>());
        final Anime anime = consumetRepository.searchAnime(filters).getResults().stream()
                .filter(result -> filterResults(result, title))
                .findFirst()
                .map(AnilistSearchResult::getId)
                .map(Integer::parseInt)
                .map(consumetRepository::getAnimeDetails)
                .map(animeMapper::toDomainObject)
                .orElseThrow(() -> new DataNotFoundException(ANIME_NOT_FOUND_ERROR));

        anime.getEpisodes().forEach(episode -> episode.setActiveEpisodeId(getActiveProvider()));
        return provisionDataFromJikan(anime);
    }

    @Cacheable(value = "animeInfo", key = "#anilistId")
    public Anime getAnimeInfo(final Integer anilistId) {
        LOGGER.info("[Consumet API] Fetching anime info for Anilist ID '{}'.", anilistId);
        final Anime anime = Optional.of(consumetRepository.getAnimeInfo(anilistId))
                .map(animeMapper::toDomainObject)
                .orElseThrow(() -> new DataNotFoundException(ANIME_NOT_FOUND_ERROR));
        anime.getEpisodes().forEach(episode -> episode.setActiveEpisodeId(getActiveProvider()));
        return provisionTrailerFromJikan(anime);
    }

    @Cacheable(value = "animeRecommendations", key = "#anilistId")
    public List<Recommendation> getRecommendationsForAnime(final int anilistId) {
        LOGGER.info("[Consumet API] Fetching anime recommendations for Anilist ID '{}'.", anilistId);
        return consumetRepository.getAnimeRecommendations(anilistId).stream()
                .map(recommendationMapper::toRecommendation)
                .toList();
    }

    @Cacheable(value = "animeNextAiring", key = "#anilistId")
    public NextAiringEpisode getNextAiringEpisode(final int anilistId) {
        LOGGER.info("[Consumet API] Fetching next airing episode for Anilist ID '{}'.", anilistId);
        final AnilistNextAiringEpisode anilistInfo = consumetRepository.getNextAiringEpisode(anilistId);
        return scheduleMapper.toNextAiringEpisode(anilistInfo);
    }

    @Cacheable(value = "streamingLinks", key = "#id + #provider")
    public StreamingLinks getStreamingsLinks(final String id, final String provider) {
        LOGGER.info("[Consumet API] Fetching streaming links for episode ID '{}' from '{}'.", id, provider);
        final AnilistStreamingLinks anilistLinks = consumetRepository.getEpisodeLinks(id, provider);

        final Map<String, Integer> qualityOrder = Map.of(
                "1080p", 0,
                "720p", 1,
                "480p", 2,
                "360p", 3,
                "default", 4,
                "backup", 5,
                "unknown", 6

        );

        final List<StreamingSource> sources = anilistLinks.getSources().stream()
                .map(source -> new StreamingSource(source.getUrl(), source.getQuality()))
                .sorted(Comparator.comparingInt(source -> qualityOrder.getOrDefault(source.getQuality(), 6)))
                .toList();

        return new StreamingLinks(anilistLinks.getHeaders().get("Referer"), sources);
    }

    public Anime getAnimeDetailsForProvider(final Integer anilistId, final String provider) {
        LOGGER.info("[Consumet API] Fetching anime details for Anilist ID '{}' from '{}'.", anilistId, provider);
        final Anime anime = Optional.of(consumetRepository.getAnimeDetails(anilistId, provider))
                .map(animeMapper::toDomainObject)
                .orElseThrow(() -> new DataNotFoundException(ANIME_NOT_FOUND_ERROR));

        anime.getEpisodes().forEach(episode -> episode.setActiveEpisodeId(getProviderByProvider(provider)));
        return provisionDataFromJikan(anime);
    }

    public List<Anime> searchAnime(final Map<String, String> filters) {
        LOGGER.info("[Consumet API] Searching anime with filters '{}'.", filters);
        return consumetRepository.searchAnime(filters).getResults().stream()
                .map(animeMapper::toDomainObject)
                .toList();
    }
    // ======================================== PRIVATE METHODS ========================================

    private static Map<String, String> applyDefaultFilters(final String input, final Map<String, String> filters) {
        final String title = input.replace("-", " ");
        final String regex = "\\s+(M{0,3})(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$";
        final Matcher matcher = Pattern.compile(regex).matcher(input.toUpperCase());

        filters.put(FORMAT_PARAM, "TV");
        filters.put(SORT_PARAM, "[\"POPULARITY_DESC\",\"TITLE_ROMAIN_DESC\"]");
        if (matcher.find()) {
            filters.put(PER_PAGE_PARAM, "1");
            filters.put(PAGE_PARAM, toInteger(matcher.group(0).trim()));
            filters.put(QUERY_PARAM, input.substring(0, input.length() - matcher.group(0).length()).trim().toLowerCase());
        } else if (title.contains(SEASON_PARAM)) {
            filters.put(QUERY_PARAM, title.substring(0, title.indexOf(SEASON_PARAM)).trim().toLowerCase());
        } else if (title.contains(PART_PARAM)) {
            filters.put(QUERY_PARAM, title.substring(0, title.indexOf(PART_PARAM)).trim().toLowerCase());
        } else {
            filters.put(QUERY_PARAM, input.toLowerCase());
        }

        return filters;
    }

    private boolean filterResults(final AnilistSearchResult result, final String title) {
        final String newTitle = result.getTitle().getRomaji().toLowerCase();
        final String originalTitle = title.toLowerCase();

        final boolean foundResult;
        if (originalTitle.contains(SEASON_PARAM)) {
            foundResult = newTitle.contains(SEASON_PARAM)
                    && newTitle.contains(originalTitle.substring(originalTitle.indexOf(SEASON_PARAM) + 6).trim());
        } else if (originalTitle.contains(PART_PARAM)) {
            foundResult = newTitle.contains(PART_PARAM)
                    && newTitle.contains(originalTitle.substring(originalTitle.indexOf(PART_PARAM) + 4).trim());
        } else {
            foundResult = true;
        }

        return foundResult;
    }

    private Anime provisionTrailerFromJikan(final Anime anime) {
        if (isBlank(anime.getTrailerUrl())) {
            final String trailerUrl = jikanRepository.getAnimeTrailer(anime.getMalId()).getYoutubeId();
            anime.setTrailerUrl(trailerUrl);
        }
        return anime;
    }

    private Anime provisionDataFromJikan(final Anime anime) {
        provisionTrailerFromJikan(anime);
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
