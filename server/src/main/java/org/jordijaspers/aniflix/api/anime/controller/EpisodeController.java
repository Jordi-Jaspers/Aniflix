package org.jordijaspers.aniflix.api.anime.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.anime.model.mapper.EpisodeMapper;
import org.jordijaspers.aniflix.api.anime.model.response.EpisodeResponse;
import org.jordijaspers.aniflix.api.anime.service.EpisodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.jordijaspers.aniflix.api.Paths.EPISODE_PATH;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The controller for the episode endpoints.
 */
@RestController
@RequiredArgsConstructor
public class EpisodeController {

    private final EpisodeService episodeService;

    private final EpisodeMapper episodeMapper;

    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = EPISODE_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<EpisodeResponse> retrieveEpisodes(@PathVariable("id") final int anilistId,
                                                            @PathVariable("episodeNumber") final int number) {
        final Episode episode = episodeService.getEpisodeOfAnime(anilistId, number);
        final EpisodeResponse episodeResponse = episodeMapper.toEpisodeResponse(episode);
        return ResponseEntity.status(OK).body(episodeResponse);
    }
}
