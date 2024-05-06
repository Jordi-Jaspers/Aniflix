package org.jordijaspers.aniflix.api.interaction.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.jordijaspers.aniflix.api.interaction.repository.InteractionRepository;
import org.jordijaspers.aniflix.api.recommendation.model.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static org.jordijaspers.aniflix.common.util.SecurityUtil.getLoggedInUser;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class UserInteractionEnhancer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInteractionEnhancer.class);

    private final InteractionRepository interactionRepository;

    public void applyAnime(final Anime anime) {
        applyAnime(List.of(anime));
    }

    public void applyAnime(final List<Anime> collection) {
        if (isEmpty(collection)) {
            LOGGER.info("No anime to apply user interactions to.");
            return;
        }

        final List<Integer> anilistIds = collection.stream().map(Anime::getAnilistId).toList();
        final List<Interaction> interactions = interactionRepository.findAllByAnilistIdIn(anilistIds, getLoggedInUser());
        interactions.forEach(interaction -> {
            collection.stream()
                    .filter(anime -> Objects.equals(anime.getAnilistId(), interaction.getAnime().getAnilistId()))
                    .findFirst()
                    .ifPresent(anime -> {
                        LOGGER.info("Applying user interactions to anime with ID '{}'.", anime.getAnilistId());
                        anime.setLiked(interaction.isLiked());
                        anime.setInLibrary(interaction.isInLibrary());
                        anime.setWatchStatus(interaction.getWatchStatus());
                        anime.setLastSeenEpisode(interaction.getLastSeenEpisode());
                        anime.setLastInteraction(interaction.getLastInteraction());
                    });
        });
    }

    public void applyRecommendation(final Recommendation anime) {
        applyRecommendation(List.of(anime));
    }

    public void applyRecommendation(final List<Recommendation> collection) {
        if (isEmpty(collection)) {
            LOGGER.info("No anime to apply user interactions to.");
            return;
        }

        final List<Integer> anilistIds = collection.stream().map(Recommendation::getAnilistId).toList();
        final List<Interaction> interactions = interactionRepository.findAllByAnilistIdIn(anilistIds, getLoggedInUser());
        interactions.forEach(interaction -> {
            collection.stream()
                    .filter(anime -> Objects.equals(anime.getAnilistId(), interaction.getAnime().getAnilistId()))
                    .findFirst()
                    .ifPresent(anime -> {
                        LOGGER.info("Applying user interactions to anime with ID '{}'.", anime.getAnilistId());
                        anime.setLiked(interaction.isLiked());
                        anime.setInLibrary(interaction.isInLibrary());
                        anime.setWatchStatus(interaction.getWatchStatus());
                        anime.setLastSeenEpisode(interaction.getLastSeenEpisode());
                        anime.setLastInteraction(interaction.getLastInteraction());
                    });
        });
    }
}
