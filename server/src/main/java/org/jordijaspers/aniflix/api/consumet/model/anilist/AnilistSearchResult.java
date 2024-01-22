package org.jordijaspers.aniflix.api.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AnilistSearchResult {

    @JsonProperty("id")
    private String id;

    @JsonProperty("malId")
    private int malId;

    @JsonProperty("title")
    private AnilistTitle title;

    @JsonProperty("description")
    private String description = "No description available.";

    @JsonProperty("totalEpisodes")
    private int totalEpisodes;

    @JsonProperty("rating")
    private int rating;

    @JsonProperty("status")
    private String status;

    @JsonProperty("image")
    private String image;

    @JsonProperty("cover")
    private String cover;

    @JsonProperty("releaseDate")
    private int releaseDate;

    @JsonProperty("genres")
    private List<String> genres;

    @JsonProperty("type")
    private String type;

    @JsonProperty("popularity")
    private int popularity;

    @JsonProperty("color")
    private String color;

    @JsonProperty("currentEpisodeCount")
    private int currentEpisodeCount;

    /**
     * Returns the status of the anime in uppercase for mapping purposes.
     */
    public String getStatus() {
        return status.toUpperCase();
    }
}
