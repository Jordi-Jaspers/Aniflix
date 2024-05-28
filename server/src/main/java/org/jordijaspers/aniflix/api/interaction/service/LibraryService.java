package org.jordijaspers.aniflix.api.interaction.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.constant.WatchStatus;
import org.jordijaspers.aniflix.api.anime.repository.AnimeRepository;
import org.jordijaspers.aniflix.api.anime.service.AnimeService;
import org.jordijaspers.aniflix.api.user.model.User;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.jordijaspers.aniflix.api.interaction.model.request.KetsuData;
import org.jordijaspers.aniflix.api.interaction.model.request.LibrarySearchRequest;
import org.jordijaspers.aniflix.api.interaction.model.specification.LibrarySpecification;
import org.jordijaspers.aniflix.api.interaction.repository.InteractionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.jordijaspers.aniflix.common.util.SecurityUtil.getLoggedInUser;

/**
 * A service to manage the library of a user.
 */
@Service
@RequiredArgsConstructor
public class LibraryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryService.class);

    private final AnimeService animeService;

    private final AnimeRepository animeRepository;

    private final InteractionService interactionService;

    private final InteractionRepository interactionRepository;

    private final UserInteractionEnhancer userInteractionEnhancer;

    public Page<Anime> searchInLibraryOfUser(final LibrarySearchRequest request) {
        final User user = getLoggedInUser();
        LOGGER.info("Searching library for user '{}' with the following params '{}'", user.getUsername(), request);
        final Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize());
        final Page<Anime> page = animeRepository.findAll(new LibrarySpecification(request, user), pageable);
        userInteractionEnhancer.applyAnime(page);
        return page;
    }

    public void removeFromLibrary(final Integer anilistId) {
        final Interaction interaction = interactionService.getInteractedAnime(anilistId);
        interaction.setInLibrary(false);
        interactionService.updateInteraction(interaction);
        LOGGER.info("Anime '{}' ('{}') successfully removed from the library for user '{}'",
                interaction.getAnime().getTitle(), anilistId, getLoggedInUser().getUsername());
    }

    public void addToLibrary(final Integer anilistId) {
        final Interaction interaction = interactionService.getInteractedAnime(anilistId);
        interaction.setInLibrary(true);
        interactionService.updateInteraction(interaction);
        LOGGER.info("Anime '{}' ('{}') successfully added to the library for user '{}'",
                interaction.getAnime().getTitle(), anilistId, getLoggedInUser().getUsername());
    }

    @Async
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
        final Interaction interaction = interactionService.getInteractedAnime(update.getAnime().getAnilistId());
        interaction.setLastSeenEpisode(update.getLastSeenEpisode());
        interactionRepository.save(interaction);
    }
}
