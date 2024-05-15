package org.jordijaspers.aniflix.api.interaction.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.service.AnimeService;
import org.jordijaspers.aniflix.api.authentication.model.User;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.jordijaspers.aniflix.api.interaction.repository.InteractionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static org.jordijaspers.aniflix.common.util.SecurityUtil.getLoggedInUser;

/**
 * A service to manage user interactions with certain Anime.
 */
@Service
@RequiredArgsConstructor
public class InteractionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InteractionService.class);

    private final AnimeService animeService;

    private final InteractionRepository interactionRepository;

    public Interaction getInteractedAnime(final Integer anilistId) {
        final User user = getLoggedInUser();
        LOGGER.info("User '{}' interacted with anime which has anilist ID '{}'.", user.getUsername(), anilistId);
        return interactionRepository.findByAnilistId(anilistId, user)
                .map(interaction -> {
                    LOGGER.info("User '{}' has interacted with anime ('{}') before.", user.getUsername(), anilistId);
                    return interaction;
                })
                .orElseGet(() -> {
                    LOGGER.info("User '{}' has not interacted with anime ('{}') yet.", user.getUsername(), anilistId);
                    return animeService.isAnimeInDatabase(anilistId)
                            ? new Interaction(user, anilistId)
                            : new Interaction(user, animeService.findDetailsByAnilistId(anilistId));
                });
    }

    public void updateInteraction(final Interaction interaction) {
        interactionRepository.save(interaction);
    }
}
