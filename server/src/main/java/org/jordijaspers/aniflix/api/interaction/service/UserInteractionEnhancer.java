package org.jordijaspers.aniflix.api.interaction.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.InteractionProperty;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.jordijaspers.aniflix.api.interaction.repository.InteractionRepository;
import org.jordijaspers.aniflix.api.recommendation.model.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static org.jordijaspers.aniflix.common.util.SecurityUtil.getLoggedInUser;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * A service to enhance anime and recommendation objects with user interactions.
 * It will apply these interactions to the transient fields of the objects.
 */
@Component
@RequiredArgsConstructor
public class UserInteractionEnhancer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInteractionEnhancer.class);

    private final InteractionRepository interactionRepository;

    public void applyAnime(final Anime anime) {
        applyInteractions(List.of(anime), Anime::getAnilistId);
    }

    public void applyAnime(final Page<Anime> animePage) {
        applyInteractions(animePage.getContent(), Anime::getAnilistId);
    }

    public void applyAnime(final List<Anime> collection) {
        applyInteractions(collection, Anime::getAnilistId);
    }

    public void applyRecommendations(final List<Recommendation> collection) {
        applyInteractions(collection, Recommendation::getAnilistId);
    }

    private <T extends InteractionProperty> void applyInteractions(final List<T> collection, final Function<T, Integer> getAnilistId) {
        if (isEmpty(collection)) {
            return;
        }

        final List<Integer> anilistIds = collection.stream().map(getAnilistId).toList();
        final List<Interaction> interactions = interactionRepository.findAllByAnilistIdIn(anilistIds, getLoggedInUser());

        interactions.forEach(interaction -> {
            collection.stream()
                    .filter(anime -> Objects.equals(getAnilistId.apply(anime), interaction.getAnime().getAnilistId()))
                    .findFirst()
                    .ifPresent(anime -> {
                        LOGGER.debug("Applying user interactions to anime with ID '{}'.", getAnilistId.apply(anime));
                        anime.setLiked(interaction.isLiked());
                        anime.setInLibrary(interaction.isInLibrary());
                        anime.setWatchStatus(interaction.getWatchStatus());
                        anime.setLastSeenEpisode(interaction.getLastSeenEpisode());
                        anime.setLastInteraction(interaction.getLastInteraction());
                    });
        });
    }
}
