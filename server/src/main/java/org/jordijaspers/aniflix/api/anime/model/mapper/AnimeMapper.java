package org.jordijaspers.aniflix.api.anime.model.mapper;

import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.anime.model.constant.AnimeStatus;
import org.jordijaspers.aniflix.api.anime.model.constant.Genres;
import org.jordijaspers.aniflix.api.anime.model.request.AnimeRequest;
import org.jordijaspers.aniflix.api.anime.model.response.AnimeResponse;
import org.jordijaspers.aniflix.api.anime.model.response.DetailedAnimeResponse;
import org.jordijaspers.aniflix.api.anime.model.response.EpisodeResponse;
import org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders;
import org.jordijaspers.aniflix.api.consumed.consumet.model.ResultPage;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistEpisode;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistInfoResult;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistOverview;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistRecentEpisode;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistSearchResult;
import org.jordijaspers.aniflix.api.consumed.consumet.service.DomainHealthChecker;
import org.jordijaspers.aniflix.api.genre.model.Genre;
import org.jordijaspers.aniflix.common.mappers.PageMapper;
import org.jordijaspers.aniflix.config.SharedMapperConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.ZoneOffset.UTC;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isNotBlank;
import static org.apache.logging.log4j.util.Strings.isNotEmpty;
import static org.jordijaspers.aniflix.api.consumed.consumet.ConsumetConstants.QueryParams.*;

/**
 * The mapper for the anime.
 */
@Mapper(config = SharedMapperConfiguration.class, imports = {AnilistProviders.class, DomainHealthChecker.class})
public abstract class AnimeMapper extends PageMapper<AnimeResponse, Anime> {

    // #################### Resource Mapping ####################

    @Named("toResourceObject")
    @Mapping(source = "imageUrl", target = "image")
    @Mapping(source = "coverUrl", target = "cover")
    @Mapping(source = "trailerUrl", target = "trailer")
    @Mapping(target = "genres", expression = "java(toGenres(anime.getGenres()))")
    @Mapping(target = "watchStatus", expression = "java(anime.getWatchStatus().getValue())")
    public abstract AnimeResponse toResourceObject(Anime anime);

    @IterableMapping(qualifiedByName = "toResourceObject")
    public abstract List<AnimeResponse> toResourceObject(List<Anime> anime);

    @Named("toResponseWithEpisodes")
    @Mapping(source = "imageUrl", target = "image")
    @Mapping(source = "coverUrl", target = "cover")
    @Mapping(target = "genres", expression = "java(toGenres(anime.getGenres()))")
    @Mapping(target = "episodes", expression = "java(toEpisodeResponse(anime))")
    @Mapping(target = "trailer", source = "trailerUrl")
    @Mapping(target = "watchStatus", expression = "java(anime.getWatchStatus().getValue())")
    public abstract DetailedAnimeResponse toResponseWithEpisodes(Anime anime);

    @IterableMapping(qualifiedByName = "toResponseWithEpisodes")
    public abstract List<DetailedAnimeResponse> toResponseWithEpisodes(List<Anime> anime);

    // #################### Domain Mapping ####################

    @Mapping(target = "anilistId", source = "id")
    @Mapping(target = "title", expression = "java(result.getTitle().getPreferredTitle())")
    @Mapping(target = "imageUrl", source = "image")
    @Mapping(target = "coverUrl", source = "cover")
    @Mapping(target = "releaseYear", source = "releaseDate")
    @Mapping(target = "mediaType", source = "type")
    public abstract Anime toDomainObject(AnilistSearchResult result);

    @Mapping(target = "anilistId", source = "id")
    @Mapping(target = "title", expression = "java(result.getTitle().getPreferredTitle())")
    @Mapping(target = "imageUrl", source = "image")
    @Mapping(target = "coverUrl", source = "cover")
    @Mapping(target = "trailerUrl", source = "trailer.id")
    @Mapping(target = "releaseYear", source = "releaseDate")
    @Mapping(target = "mediaType", source = "type")
    @Mapping(target = "status", expression = "java(toStatus(result.getStatus()))")
    public abstract Anime toDomainObject(AnilistOverview result);

    @Mapping(target = "anilistId", source = "id")
    @Mapping(target = "title", expression = "java(result.getTitle().getPreferredTitle())")
    @Mapping(target = "imageUrl", source = "image")
    @Mapping(target = "coverUrl", source = "cover")
    @Mapping(target = "trailerUrl", source = "trailer.id")
    @Mapping(target = "releaseYear", source = "releaseDate")
    @Mapping(target = "mediaType", source = "type")
    public abstract Anime toDomainObject(AnilistInfoResult result);

    // #################### Page Mapping ####################

    public Page<Anime> toOverviewPage(final ResultPage<AnilistOverview> source) {
        final List<Anime> content = source.getResults().stream().map(this::toDomainObject).toList();
        final Pageable pageable = PageRequest.of(source.getCurrentPage(), source.getResults().size());
        return new PageImpl<>(content, pageable, source.getTotalResults());
    }

    public Page<Anime> toSearchResultPage(final ResultPage<AnilistSearchResult> source) {
        final List<Anime> content = source.getResults().stream().map(this::toDomainObject).toList();
        final Pageable pageable = PageRequest.of(source.getCurrentPage(), source.getResults().size());
        return new PageImpl<>(content, pageable, source.getTotalResults());
    }

    public Page<Anime> toInfoPage(final ResultPage<AnilistInfoResult> source) {
        final List<Anime> content = source.getResults().stream().map(this::toDomainObject).toList();
        final Pageable pageable = PageRequest.of(source.getCurrentPage(), source.getResults().size());
        return new PageImpl<>(content, pageable, source.getTotalResults());
    }

    // #################### Anilist Episodes ####################

    @Mapping(target = "episodeId", source = "id")
    @Mapping(target = "title", defaultExpression = "java(\"Episode \" + source.getNumber())")
    @Mapping(target = "id", ignore = true)
    public abstract Episode toEpisode(AnilistEpisode source);

    @Named("toRecentEpisodesResponse")
    @Mapping(target = "title", expression = "java(source.getTitle().getPreferredTitle())")
    public abstract EpisodeResponse toRecentEpisodesResponse(AnilistRecentEpisode source);

    @IterableMapping(qualifiedByName = "toRecentEpisodesResponse")
    public abstract List<EpisodeResponse> toRecentEpisodesResponse(List<AnilistRecentEpisode> source);

    /**
     * Converts a list of anime to a list of anime responses.
     */
    public List<EpisodeResponse> toEpisodeResponse(final Anime anime) {
        return anime.getEpisodes().stream()
                .map(episode -> {
                    final EpisodeResponse response = new EpisodeResponse();
                    response.setAnilistId(anime.getAnilistId());
                    response.setTitle(anime.getTitle());
                    response.setEpisodeTitle(episode.getTitle());
                    response.setEpisodeNumber(episode.getNumber());
                    response.setDuration(episode.getDuration());
                    if (nonNull(episode.getAirDate())) {
                        response.setAirDate(ZonedDateTime.of(episode.getAirDate(), UTC));
                    }
                    return response;
                })
                .sorted(Comparator.comparingInt(EpisodeResponse::getEpisodeNumber))
                .toList();
    }

    /**
     * Converts a request to a map of filters.
     */
    public Map<String, String> toFilters(final AnimeRequest request) {
        return Stream.of(
                        pair(QUERY_PARAM, request.getTitle()),
                        pair(PAGE_PARAM, String.valueOf(request.getPage())),
                        pair(PER_PAGE_PARAM, String.valueOf(request.getPerPage())),
                        pair(GENRES_PARAM, nonNull(request.getGenre()) ? String.valueOf(request.getGenre()) : ""),
                        pair(SEASON_PARAM, nonNull(request.getSeason()) ? String.valueOf(request.getSeason()) : "")
                )
                .filter(entry -> isNotBlank(entry.getValue()) && isNotEmpty(entry.getValue()) && !ZERO_VALUE.equals(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Converts a list of anime to a Map of anime by genre.
     */
    protected Map<String, List<AnimeResponse>> toAnimeByGenreResponse(final Map<Genre, List<Anime>> animeByGenre) {
        return animeByGenre.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getName().toString(),
                        entry -> toResourceObject(entry.getValue())
                ));
    }

    /**
     * Maps a String to status entity if possible.
     */
    protected AnimeStatus toStatus(final String status) {
        return AnimeStatus.ofName(status);
    }

    /**
     * Maps a String to genre entity if possible.
     */
    protected Set<Genre> toGenres(final List<String> genres) {
        return genres.stream()
                .map(Genres::enumOf)
                .map(Genre::new)
                .collect(Collectors.toSet());
    }

    protected List<String> toGenres(final Set<Genre> genres) {
        return genres.stream()
                .map(Genre::getName)
                .map(Genres::toString)
                .collect(Collectors.toList());
    }

    private Map.Entry<String, String> pair(final String key, final String value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }
}
