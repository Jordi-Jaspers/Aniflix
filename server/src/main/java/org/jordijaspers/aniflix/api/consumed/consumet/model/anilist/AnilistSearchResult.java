package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

import static java.util.Objects.nonNull;

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

    /**
     * Returns the title of the anime in the language you prefer.
     */
    public String getPreferredTitle() {
        if (nonNull(title.getEnglish())) {
            return title.getEnglish().toLowerCase();
        } else if (nonNull(title.getRomaji())) {
            return title.getRomaji().toLowerCase();
        } else if (nonNull(title.getJapanse())) {
            return title.getJapanse().toLowerCase();
        } else {
            return "";
        }
    }
}
