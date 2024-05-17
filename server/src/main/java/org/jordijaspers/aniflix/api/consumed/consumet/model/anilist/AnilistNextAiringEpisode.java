package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The next airing episode of an anime.
 */
@Data
public class AnilistNextAiringEpisode {

    @JsonProperty("airingTime")
    private int airingTime;
    
    @JsonProperty("timeUntilAiring")
    private int timeUntilAiring;
    
    @JsonProperty("episode")
    private int episode;

}
