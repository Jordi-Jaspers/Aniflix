package org.jordijaspers.aniflix.api.interaction.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.interaction.model.request.InteractionRequest;
import org.jordijaspers.aniflix.api.interaction.service.InteractionService;
import org.jordijaspers.aniflix.security.principal.UserTokenPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.jordijaspers.aniflix.api.Paths.ANIME_LIKED_PATH;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final InteractionService interactionService;

    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = ANIME_LIKED_PATH, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> likeAnime(@AuthenticationPrincipal final UserTokenPrincipal principal,
                                          @RequestBody final InteractionRequest request) {
        interactionService.addToLikes(new Anime(request.getAnilistId()), principal.getUser());
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping(path = ANIME_LIKED_PATH, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> unlikeAnime(@AuthenticationPrincipal final UserTokenPrincipal principal,
                                            @RequestBody final InteractionRequest request) {
        interactionService.removeFromLikes(new Anime(request.getAnilistId()), principal.getUser());
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
