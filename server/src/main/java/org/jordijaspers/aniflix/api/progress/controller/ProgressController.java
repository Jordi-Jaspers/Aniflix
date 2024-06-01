package org.jordijaspers.aniflix.api.progress.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.response.EpisodeResponse;
import org.jordijaspers.aniflix.api.anime.service.EpisodeService;
import org.jordijaspers.aniflix.api.progress.model.request.EpisodeProgressRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.jordijaspers.aniflix.api.Paths.PROGRESS_PATH;
import static org.jordijaspers.aniflix.common.util.SecurityUtil.getLoggedInUser;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * The controller to show the progress of the episodes.
 */
@RestController
@RequiredArgsConstructor
public class ProgressController {

    private final EpisodeService episodeService;

    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping(path = PROGRESS_PATH)
    public ResponseEntity<EpisodeResponse> updateProgress(@RequestBody final EpisodeProgressRequest request) {
        episodeService.updateProgress(request, getLoggedInUser());
        return ResponseEntity.status(NO_CONTENT).build();
    }

}
