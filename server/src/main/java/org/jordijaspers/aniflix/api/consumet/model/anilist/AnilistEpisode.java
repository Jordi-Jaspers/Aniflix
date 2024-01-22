package org.jordijaspers.aniflix.api.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static org.jordijaspers.aniflix.api.consumet.ConsumetConstants.Constants.SLASH;

@Data
public class AnilistEpisode {

    @JsonProperty("id")
    private String urlId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("number")
    private Long number;

    @JsonProperty("image")
    private String image;

    /**
     * Returns the urlId with a leading slash.
     */
    public String getUrlId() {
        return urlId.startsWith(SLASH)
                ? urlId
                : SLASH + urlId;
    }
}
