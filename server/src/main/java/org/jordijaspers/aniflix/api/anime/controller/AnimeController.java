package org.jordijaspers.aniflix.api.anime.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.mapper.AnimeMapper;
import org.jordijaspers.aniflix.api.anime.model.request.AnimeRequest;
import org.jordijaspers.aniflix.api.anime.model.request.GenreRequest;
import org.jordijaspers.aniflix.api.anime.model.request.PageableRequest;
import org.jordijaspers.aniflix.api.anime.model.response.AnimeResponse;
import org.jordijaspers.aniflix.api.anime.model.response.DetailedAnimeResponse;
import org.jordijaspers.aniflix.api.anime.model.response.EpisodeResponse;
import org.jordijaspers.aniflix.api.anime.service.AnimeService;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistRecentEpisode;
import org.jordijaspers.aniflix.common.mappers.model.PageResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.jordijaspers.aniflix.api.Paths.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The controller for the anime endpoints.
 */
@RestController
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    private final AnimeMapper animeMapper;

    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping(path = ANIME_SEARCH, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AnimeResponse>> searchAnime(@RequestBody final AnimeRequest request) {
        final Map<String, String> filters = animeMapper.toFilters(request);
        final List<Anime> anime = animeService.searchAnime(filters);
        return ResponseEntity.status(OK).body(animeMapper.toResourceObject(anime));
    }

    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping(path = ANIME_DETAILS, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DetailedAnimeResponse> getAnimeDetails(@PathVariable("id") final int anilistId) {
        final Anime anime = animeService.findDetailsByAnilistId(anilistId);
        return ResponseEntity.status(OK).body(animeMapper.toResponseWithEpisodes(anime));
    }

    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping(path = ANIME_INFO, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DetailedAnimeResponse> getAnimeInfo(@PathVariable("id") final int anilistId) {
        final Anime anime = animeService.findInfoByAnilistId(anilistId);
        return ResponseEntity.status(OK).body(animeMapper.toResponseWithEpisodes(anime));
    }

    // ======================================== ANIME OVERVIEW ========================================

    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping(path = ANIME_BANNER, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AnimeResponse> getAnimeBanner() {
        final Anime anime = animeService.getAnimeBanner();
        return ResponseEntity.status(OK).body(animeMapper.toResourceObject(anime));
    }

    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping(path = ANIME_RECENT, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EpisodeResponse>> getRecentAnime(@RequestBody final PageableRequest request) {
        final List<AnilistRecentEpisode> recentEpisodes = animeService.getAnimeOfRecentEpisodes(request.getPerPage(), request.getPage());
        return ResponseEntity.status(OK).body(animeMapper.toRecentEpisodesResponse(recentEpisodes));
    }

    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping(path = ANIME_POPULAR, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResource<AnimeResponse>> getPopularAnime(@RequestBody final PageableRequest request) {
        final Page<Anime> popularAnime = animeService.getPopularAnime(request.getPerPage(), request.getPage());
        return ResponseEntity.status(OK).body(animeMapper.toPageResource(popularAnime));
    }

    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping(path = ANIME_TRENDING, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResource<AnimeResponse>> getTrendingAnime(@RequestBody final PageableRequest request) {
        final Page<Anime> trendingAnime = animeService.getTrendingAnime(request.getPerPage(), request.getPage());
        return ResponseEntity.status(OK).body(animeMapper.toPageResource(trendingAnime));
    }

    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping(path = ANIME_GENRE, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResource<AnimeResponse>> getAnimeByGenre(@RequestBody final GenreRequest request) {
        final Page<Anime> animeByGenre = animeService.getAnimeByGenre(request.getGenre(), request.getPerPage(), request.getPage());
        return ResponseEntity.status(OK).body(animeMapper.toPageResource(animeByGenre));
    }
}
