package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The source of an anime.
 */
@Data
public class AnilistSource {

    @JsonProperty("url")
    private String url;

    @JsonProperty("isM3U8")
    private boolean isM3U8;

    @JsonProperty("quality")
    private String quality = "Backup";

}
