package org.jordijaspers.aniflix.api.interaction.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static org.jordijaspers.aniflix.common.util.SecurityUtil.getLoggedInUser;

/**
 * A service to manage user interactions with certain Anime.
 */
@Service
@RequiredArgsConstructor
public class LikesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LikesService.class);

    private final InteractionService interactionService;

    public void removeFromLikes(final Integer anilistId) {
        final Interaction interaction = interactionService.getInteractedAnime(anilistId);
        interaction.setLiked(false);
        interactionService.updateInteraction(interaction);
        LOGGER.info("User '{}' unliked anime '{}' ('{}')", getLoggedInUser().getUsername(), interaction.getAnime().getTitle(), anilistId);
    }

    public void addToLikes(final Integer anilistId) {
        final Interaction interaction = interactionService.getInteractedAnime(anilistId);
        interaction.setLiked(true);
        interactionService.updateInteraction(interaction);
        LOGGER.info("User '{}' liked anime '{}' ('{}')", getLoggedInUser().getUsername(), interaction.getAnime().getTitle(), anilistId);
    }
}
