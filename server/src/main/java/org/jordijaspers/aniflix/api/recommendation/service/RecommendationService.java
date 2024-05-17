package org.jordijaspers.aniflix.api.recommendation.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.consumed.consumet.service.ConsumetService;
import org.jordijaspers.aniflix.api.interaction.service.UserInteractionEnhancer;
import org.jordijaspers.aniflix.api.recommendation.model.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A service to manage recommendations for anime.
 */
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationService.class);

    private final UserInteractionEnhancer userInteractionEnhancer;

    private final ConsumetService consumetService;

    public List<Recommendation> getAnimeRecommendations(final int anilistId) {
        LOGGER.info("Getting recommendations for anime with Anilist ID '{}'.", anilistId);
        final List<Recommendation> recommendations = consumetService.getRecommendationsForAnime(anilistId);
        userInteractionEnhancer.applyRecommendations(recommendations);
        return recommendations;
    }
}
