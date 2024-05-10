package org.jordijaspers.aniflix.api.interaction.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.interaction.service.LikesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.jordijaspers.aniflix.api.Paths.LIKE_ANIME_PATH;
import static org.jordijaspers.aniflix.api.Paths.UNLIKE_ANIME_PATH;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * The controller for likes.
 */
@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = LIKE_ANIME_PATH)
    public ResponseEntity<Void> likeAnime(@PathVariable("id") final int anilistId) {
        likesService.addToLikes(anilistId);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = UNLIKE_ANIME_PATH)
    public ResponseEntity<Void> dislikeAnime(@PathVariable("id") final int anilistId) {
        likesService.removeFromLikes(anilistId);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
