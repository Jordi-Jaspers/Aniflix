package org.jordijaspers.aniflix.api.anime.model.mapper;

import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.anime.model.constant.AnimeStatus;
import org.jordijaspers.aniflix.api.anime.model.constant.Genres;
import org.jordijaspers.aniflix.api.anime.model.request.AnimeRequest;
import org.jordijaspers.aniflix.api.anime.model.response.AnimeResponse;
import org.jordijaspers.aniflix.api.anime.model.response.DetailedAnimeResponse;
import org.jordijaspers.aniflix.api.anime.model.response.EpisodeResponse;
import org.jordijaspers.aniflix.api.consumet.model.anilist.AnilistEpisode;
import org.jordijaspers.aniflix.api.consumet.model.anilist.AnilistInfoResult;
import org.jordijaspers.aniflix.api.consumet.model.anilist.AnilistOverview;
import org.jordijaspers.aniflix.api.consumet.model.anilist.AnilistRecentEpisode;
import org.jordijaspers.aniflix.api.consumet.model.anilist.AnilistSearchResult;
import org.jordijaspers.aniflix.api.genre.model.Genre;
import org.jordijaspers.aniflix.config.SharedMapperConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isNotEmpty;
import static org.jordijaspers.aniflix.api.consumet.ConsumetConstants.QueryParams.*;

@Mapper(config = SharedMapperConfiguration.class)
public abstract class AnimeMapper {

    @Mapping(target = "anilistId", source = "id")
    @Mapping(target = "title", expression = "java(result.getPreferredTitle())")
    @Mapping(target = "imageUrl", source = "image")
    @Mapping(target = "coverUrl", source = "cover")
    @Mapping(target = "releaseYear", source = "releaseDate")
    @Mapping(target = "mediaType", source = "type")
    public abstract Anime toAnime(AnilistSearchResult result);

    @Mapping(target = "anilistId", source = "id")
    @Mapping(target = "title", expression = "java(result.getPreferredTitle())")
    @Mapping(target = "imageUrl", source = "image")
    @Mapping(target = "coverUrl", source = "cover")
    @Mapping(target = "trailerUrl", source = "trailer.id")
    @Mapping(target = "releaseYear", source = "releaseDate")
    @Mapping(target = "mediaType", source = "type")
    @Mapping(target = "status", expression = "java(toStatus(result.getStatus()))")
    public abstract Anime toAnime(AnilistOverview result);

    @Mapping(target = "anilistId", source = "id")
    @Mapping(target = "title", expression = "java(result.getPreferredTitle())")
    @Mapping(target = "imageUrl", source = "image")
    @Mapping(target = "coverUrl", source = "cover")
    @Mapping(target = "trailerUrl", source = "trailer.id")
    @Mapping(target = "releaseYear", source = "releaseDate")
    @Mapping(target = "mediaType", source = "type")
    public abstract Anime toAnime(AnilistInfoResult result);

    @Mapping(target = "title", defaultExpression = "java(\"Episode \" + source.getNumber())")
    public abstract Episode toEpisode(AnilistEpisode source);

    @Named("toRecentEpisodesResponse")
    @Mapping(target = "title", expression = "java(source.getPreferredTitle())")
    public abstract EpisodeResponse toRecentEpisodesResponse(AnilistRecentEpisode source);

    @IterableMapping(qualifiedByName = "toRecentEpisodesResponse")
    public abstract List<EpisodeResponse> toRecentEpisodesResponse(List<AnilistRecentEpisode> source);

    @Named("toResponseWithoutEpisodes")
    @Mapping(source = "anilistId", target = "id")
    @Mapping(source = "imageUrl", target = "image")
    @Mapping(source = "coverUrl", target = "cover")
    @Mapping(source = "trailerUrl", target = "trailer")
    @Mapping(target = "genres", expression = "java(toGenres(anime.getGenres()))")
    public abstract AnimeResponse toResponseWithoutEpisodes(Anime anime);

    @IterableMapping(qualifiedByName = "toResponseWithoutEpisodes")
    public abstract List<AnimeResponse> toResponseWithoutEpisodes(List<Anime> anime);

    @Named("toResponseWithEpisodes")
    @Mapping(source = "anilistId", target = "id")
    @Mapping(source = "imageUrl", target = "image")
    @Mapping(source = "coverUrl", target = "cover")
    @Mapping(target = "genres", expression = "java(toGenres(anime.getGenres()))")
    public abstract DetailedAnimeResponse toResponseWithEpisodes(Anime anime);

    @IterableMapping(qualifiedByName = "toResponseWithEpisodes")
    public abstract List<DetailedAnimeResponse> toResponseWithEpisodes(List<Anime> anime);

    public Map<String, String> toFilters(final AnimeRequest request) {
        return Stream.of(
                        pair(QUERY_PARAM, request.getTitle()),
                        pair(GENRES_PARAM, String.valueOf(request.getGenre())),
                        pair(SEASON_PARAM, String.valueOf(request.getSeason())),
                        pair(PAGE_PARAM, String.valueOf(request.getPage())),
                        pair(PER_PAGE_PARAM, String.valueOf(request.getPerPage()))
                )
                .filter(entry -> nonNull(entry.getValue()) && isNotEmpty(entry.getValue()) && !ZERO_VALUE.equals(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Converts a list of anime to a Map of anime by genre.
     */
    protected Map<String, List<AnimeResponse>> toAnimeByGenreResponse(final Map<Genre, List<Anime>> animeByGenre) {
        return animeByGenre.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getName().toString(),
                        entry -> toResponseWithoutEpisodes(entry.getValue())
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
