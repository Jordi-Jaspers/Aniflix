package org.jordijaspers.aniflix.api.schedule.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The next airing episode.
 */
@Data
public class NextAiringEpisode implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    private boolean hasNextEpisode;

    private LocalDateTime airingTime;

    private long timeUntilAiring;

    private int episode;

}
