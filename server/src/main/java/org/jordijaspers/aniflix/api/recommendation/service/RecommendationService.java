package org.jordijaspers.aniflix.api.recommendation.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.consumed.consumet.service.ConsumetService;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.jordijaspers.aniflix.api.interaction.repository.InteractionRepository;
import org.jordijaspers.aniflix.api.recommendation.model.Recommendation;
import org.jordijaspers.aniflix.api.recommendation.model.mapper.RecommendationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static org.jordijaspers.aniflix.common.util.SecurityUtil.getLoggedInUser;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationService.class);

    private final InteractionRepository interactionRepository;

    private final ConsumetService consumetService;

    public List<Recommendation> getAnimeRecommendations(final int anilistId) {
        LOGGER.info("Getting recommendations for anime with Anilist ID '{}'.", anilistId);
        final List<Recommendation> recommendations = consumetService.getRecommendationsForAnime(anilistId);
        applyUserInteractions(recommendations);
        return recommendations;
    }

    private void applyUserInteractions(final List<Recommendation> collection) {
        final List<Integer> anilistIds = collection.stream().map(Recommendation::getAnilistId).toList();
        final List<Interaction> interactions = interactionRepository.findAllByAnilistIdIn(anilistIds, getLoggedInUser());
        interactions.forEach(interaction -> {
            collection.stream()
                    .filter(recommendation -> Objects.equals(recommendation.getAnilistId(), interaction.getAnime().getAnilistId()))
                    .findFirst()
                    .ifPresent(recommendation -> {
                        LOGGER.info("Applying user interactions to recommendation for anime '{}'.", recommendation.getTitle());
                        recommendation.setLiked(interaction.isLiked());
                        recommendation.setInLibrary(interaction.isInLibrary());
                        recommendation.setWatchStatus(interaction.getWatchStatus());
                        recommendation.setLastSeenEpisode(interaction.getLastSeenEpisode());
                    });
        });
    }
}
