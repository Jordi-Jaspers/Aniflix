package org.jordijaspers.aniflix.api.consumed.anizip.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * The response object containing the information of an anime information from ani.zip.
 */
@Data
public class AnizipEpisode {

    @JsonProperty("episode")
    private int episode;

    @JsonProperty("anidbEid")
    private int anidbEid;

    @JsonProperty("length")
    private int length;

    @JsonProperty("airdate")
    private String airdate;

    @JsonProperty("rating")
    private double rating;

    @JsonProperty("titles")
    private String title;

    @JsonProperty("summary")
    private String summary;

    public void setTitle(final Map<String, String> title) {
        this.title = title.get("en");
    }
}
