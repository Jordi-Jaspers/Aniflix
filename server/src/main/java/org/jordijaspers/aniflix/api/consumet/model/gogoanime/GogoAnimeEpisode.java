package org.jordijaspers.aniflix.api.consumet.model.gogoanime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static org.jordijaspers.aniflix.api.consumet.ConsumetConstants.Constants.SLASH;

@Data
public class GogoAnimeEpisode {

    @JsonProperty("id")
    private String gogoId;

    @JsonProperty("episodeId")
    private String urlId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("episodeNumber")
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
