package org.jordijaspers.aniflix.api.recommendation.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistRecommendation;
import org.jordijaspers.aniflix.api.consumed.consumet.service.ConsumetService;
import org.jordijaspers.aniflix.api.recommendation.model.mapper.RecommendationMapper;
import org.jordijaspers.aniflix.api.recommendation.model.response.RecommendationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.jordijaspers.aniflix.api.Paths.ANIME_RECOMMENDATIONS;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class RecommendationController {

    private final ConsumetService consumetService;

    private final RecommendationMapper recommendationMapper;

    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = ANIME_RECOMMENDATIONS, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecommendationResponse>> getAnimeRecommendations(@PathVariable("id") final int anilistId) {
        final List<AnilistRecommendation> recommendations = consumetService.getRecommendationsForAnime(anilistId);
        return ResponseEntity.status(OK).body(recommendationMapper.toRecommendationResponses(recommendations));
    }
}
