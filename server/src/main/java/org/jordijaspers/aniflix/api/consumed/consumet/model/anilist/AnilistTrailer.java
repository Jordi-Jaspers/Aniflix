package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The trailer of an anime.
 */
@Data
public class AnilistTrailer {

    @JsonProperty("id")
    private String id;

    @JsonProperty("site")
    private String site;

    @JsonProperty("thumbnail")
    private String thumbnail;

}
