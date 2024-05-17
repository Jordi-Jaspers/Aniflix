package org.jordijaspers.aniflix.api.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.schedule.model.NextAiringEpisode;
import org.jordijaspers.aniflix.api.schedule.model.mapper.ScheduleMapper;
import org.jordijaspers.aniflix.api.schedule.model.response.NextAiringEpisodeResponse;
import org.jordijaspers.aniflix.api.schedule.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.jordijaspers.aniflix.api.Paths.ANIME_NEXT_EPISODE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The controller for the recommendations.
 */
@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    private final ScheduleMapper scheduleMapper;

    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = ANIME_NEXT_EPISODE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NextAiringEpisodeResponse> getAnimeRecommendations(@PathVariable("id") final int anilistId) {
        final NextAiringEpisode nextAiringEpisode = scheduleService.getNextAiringEpisode(anilistId);
        return ResponseEntity.status(OK).body(scheduleMapper.toNextAiringEpisodeResponse(nextAiringEpisode));
    }
}
