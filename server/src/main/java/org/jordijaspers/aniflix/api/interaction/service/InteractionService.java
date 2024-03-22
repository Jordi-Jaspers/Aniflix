package org.jordijaspers.aniflix.api.interaction.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.service.AnimeService;
import org.jordijaspers.aniflix.api.authentication.model.User;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.jordijaspers.aniflix.api.interaction.repository.InteractionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InteractionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InteractionService.class);

    private final InteractionRepository interactionRepository;

    private final AnimeService animeService;

    @Transactional
    public Interaction getInteractedAnime(final Anime anime, final User user) {
        return interactionRepository.findByAnilistId(anime, user)
                .map(interaction -> {
                    LOGGER.info("User '{}' has interacted with anime ('{}') before.", user.getUsername(), anime.getAnilistId());
                    final Anime updatedAnime = animeService.findByAnilistId(interaction.getAnime().getAnilistId());
                    interaction.setAnime(updatedAnime);
                    return interaction;
                })
                .orElseGet(() -> {
                    LOGGER.info("User '{}' has not interacted with anime ('{}') yet.", user.getUsername(), anime.getAnilistId());
                    return addToInteractions(new Interaction(user, anime));
                });
    }

    public void updateInteraction(final Interaction interaction) {
        interactionRepository.save(interaction);
    }

    private Interaction addToInteractions(final Interaction interaction) {
        final Anime anime = animeService.findByAnilistId(interaction.getAnilistId());
        interaction.setAnime(anime);
        return interactionRepository.save(interaction);
    }
}
