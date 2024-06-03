package org.jordijaspers.aniflix.api.recommendation.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.recommendation.model.Recommendation;
import org.jordijaspers.aniflix.api.recommendation.model.mapper.RecommendationMapper;
import org.jordijaspers.aniflix.api.recommendation.model.response.RecommendationResponse;
import org.jordijaspers.aniflix.api.recommendation.service.RecommendationService;
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

/**
 * The controller for the recommendations.
 */
@RestController
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    private final RecommendationMapper recommendationMapper;

    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping(path = ANIME_RECOMMENDATIONS, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecommendationResponse>> getAnimeRecommendations(@PathVariable("id") final int anilistId) {
        final List<Recommendation> recommendations = recommendationService.getAnimeRecommendations(anilistId);
        return ResponseEntity.status(OK).body(recommendationMapper.toRecommendationsResponses(recommendations));
    }
}
