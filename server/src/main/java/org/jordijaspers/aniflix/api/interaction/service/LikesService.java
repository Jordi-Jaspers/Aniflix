package org.jordijaspers.aniflix.api.interaction.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.authentication.model.User;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static org.jordijaspers.aniflix.common.util.SecurityUtil.getLoggedInUser;

@Service
@RequiredArgsConstructor
public class LikesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LikesService.class);

    private final InteractionService interactionService;

    public void removeFromLikes(final Anime anime) {
        final User user = getLoggedInUser();
        final Interaction interaction = interactionService.getInteractedAnime(anime, user);
        interaction.setLiked(false);
        interactionService.updateInteraction(interaction);
        LOGGER.info("User '{}' unliked anime '{}'", user.getUsername(), interaction.getAnime().getTitle());
    }

    public void addToLikes(final Anime anime) {
        final User user = getLoggedInUser();
        final Interaction interaction = interactionService.getInteractedAnime(anime, user);
        interaction.setLiked(true);
        interactionService.updateInteraction(interaction);
        LOGGER.info("User '{}' liked anime '{}'", getLoggedInUser().getUsername(), interaction.getAnime().getTitle());
    }
}
