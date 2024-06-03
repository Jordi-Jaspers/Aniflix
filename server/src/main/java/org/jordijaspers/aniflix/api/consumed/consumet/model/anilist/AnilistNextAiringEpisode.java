package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The next airing episode of an anime.
 */
@Data
public class AnilistNextAiringEpisode implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @JsonProperty("airingTime")
    private int airingTime;
    
    @JsonProperty("timeUntilAiring")
    private int timeUntilAiring;
    
    @JsonProperty("episode")
    private int episode;

}
