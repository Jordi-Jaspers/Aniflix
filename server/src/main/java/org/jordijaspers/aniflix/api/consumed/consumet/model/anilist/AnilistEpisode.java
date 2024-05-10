package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The episode of an anime.
 */
@Data
public class AnilistEpisode {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("number")
    private Long number;

    @JsonProperty("image")
    private String image;

}
