package org.jordijaspers.aniflix.api.schedule.model.response;

import lombok.Data;

import java.time.ZonedDateTime;

/**
 * The response for the next airing episode.
 */
@Data
public class NextAiringEpisodeResponse {

    private boolean hasNextEpisode;

    private ZonedDateTime airingTime;

    private long timeUntilAiring;

    private int episode;

}
