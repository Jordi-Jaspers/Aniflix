package org.jordijaspers.aniflix.api.interaction.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.constant.WatchStatus;
import org.jordijaspers.aniflix.api.anime.service.AnimeService;
import org.jordijaspers.aniflix.api.authentication.model.User;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.jordijaspers.aniflix.api.interaction.model.request.KetsuData;
import org.jordijaspers.aniflix.api.interaction.repository.InteractionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class InteractionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InteractionService.class);

    private final InteractionRepository interactionRepository;

    private final AnimeService animeService;

    // ==================== INTERACTION ====================

    public Interaction getInteractedAnime(final int anilistId, final User user) {
        return getInteractedAnime(new Anime(anilistId), user);
    }

    public Interaction getInteractedAnime(final Anime anime, final User user) {
        LOGGER.info("Getting anime details for anime with Anilist ID '{}' for user '{}'", anime.getAnilistId(), user.getUsername());
        return interactionRepository.findByAnilistId(anime.getAnilistId(), user)
                .map(interaction -> {
                    LOGGER.info("User '{}' has interacted with anime ('{}') before.", user.getUsername(), anime.getAnilistId());
                    return updateInteraction(interaction);
                })
                .orElseGet(() -> {
                    LOGGER.info("User '{}' has not interacted with anime ('{}') yet.", user.getUsername(), anime.getAnilistId());
                    return addToInteractions(new Interaction(user, anime));
                });
    }

    public Interaction addToInteractions(final Interaction interaction) {
        final Anime anime = animeService.findByAnilistId(interaction.getAnilistId());
        interaction.setAnime(anime);
        return interactionRepository.save(interaction);
    }

    public Interaction updateInteraction(final Interaction interaction) {
        animeService.updateAnimeInfo(interaction.getAnime());
        return interactionRepository.save(interaction);
    }

    // ==================== LIKED ====================

    public void removeFromLikes(final Anime anime, final User user) {
        final Interaction interaction = getInteractedAnime(anime, user);
        interaction.setLiked(false);
        interactionRepository.save(interaction);
        LOGGER.info("User '{}' unliked anime '{}'", user.getUsername(), anime.getTitle());
    }

    public void addToLikes(final Anime anime, final User user) {
        final Interaction interaction = getInteractedAnime(anime, user);
        interaction.setLiked(true);
        interactionRepository.save(interaction);
        LOGGER.info("User '{}' liked anime '{}'", user.getUsername(), anime.getTitle());
    }

    // ==================== LIBRARY ====================

    public List<Interaction> getFullLibraryForUser(final User user) {
        LOGGER.debug("Getting library for user '{}'", user.getUsername());
        return interactionRepository.findAllLibraryForUser(user);
    }

    public List<Interaction> searchInLibraryOfUser(final String title, final User user) {
        LOGGER.debug("Searching library for user '{}' with title '{}'", user.getUsername(), title);
        return isBlank(title)
                ? interactionRepository.findAllLibraryForUser(user)
                : interactionRepository.searchLibraryByTitleForUser(title, user);
    }

    public void removeFromLibrary(final Anime anime, final User user) {
        final Interaction interaction = getInteractedAnime(anime, user);
        interaction.setInLibrary(false);
        interactionRepository.save(interaction);
        LOGGER.info("Anime '{}' successfully removed from library for user '{}'", anime.getTitle(), user.getUsername());
    }

    public void addToLibrary(final Anime anime, final User user) {
        final Interaction interaction = getInteractedAnime(anime, user);
        interaction.setInLibrary(true);
        interactionRepository.save(interaction);
        LOGGER.info("Anime '{}' successfully added to library for user '{}'", anime.getTitle(), user.getUsername());
    }

    @Async("anime.import")
    public void importLibrary(final List<KetsuData> library, final User user) {
        LOGGER.info("Attempting to import library with '{}' entries", library.size());
        int failed = 0;
        final long start = System.currentTimeMillis();
        for (final KetsuData entry : library) {
            try {
                final Anime anime = animeService.findAnimeByTitle(entry.getTitle());
                final WatchStatus status = WatchStatus.valueOf(entry.getCategories().get(0));
                final Interaction interaction = new Interaction(user, anime);
                interaction.setWatchStatus(status);
                importInteraction(interaction);
            } catch (final Exception exception) {
                LOGGER.error("Failed to import anime '{}' for user '{}'", entry.getTitle(), user.getUsername(), exception);
                failed++;
            }
        }
        final long end = System.currentTimeMillis();
        LOGGER.info("Imported library with '{}' entries in {}ms ({} failed)", library.size(), end - start, failed);
    }

    private void importInteraction(final Interaction update) {
        final Interaction interaction = getInteractedAnime(update.getAnime(), update.getUser());
        interaction.setLastSeenEpisode(update.getLastSeenEpisode());
        interactionRepository.save(interaction);
    }
}
