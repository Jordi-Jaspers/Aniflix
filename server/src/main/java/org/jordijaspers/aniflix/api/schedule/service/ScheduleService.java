package org.jordijaspers.aniflix.api.schedule.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.consumed.consumet.service.ConsumetService;
import org.jordijaspers.aniflix.api.schedule.model.NextAiringEpisode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * A service to get the schedule of an anime.
 */
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);

    private final ConsumetService consumetService;

    public NextAiringEpisode getNextAiringEpisode(final int anilistId) {
        LOGGER.info("Getting recommendations for anime with Anilist ID '{}'.", anilistId);
        return consumetService.getNextAiringEpisode(anilistId);
    }
}
