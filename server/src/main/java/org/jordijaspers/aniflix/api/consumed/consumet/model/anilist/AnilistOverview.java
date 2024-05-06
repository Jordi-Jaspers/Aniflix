package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Data
public class AnilistOverview {

    @JsonProperty("id")
    private String id;

    @JsonProperty("malId")
    private int malId;

    @JsonProperty("title")
    private AnilistTitle title;

    @JsonProperty("image")
    private String image;

    @JsonProperty("trailer")
    private AnilistTrailer trailer;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private String status;

    @JsonProperty("cover")
    private String cover;

    @JsonProperty("rating")
    private int rating;

    @JsonProperty("releaseDate")
    private int releaseDate;

    @JsonProperty("color")
    private String color;

    @JsonProperty("genres")
    private List<String> genres = new ArrayList<>();

    @JsonProperty("totalEpisodes")
    private int totalEpisodes;

    @JsonProperty("duration")
    private int duration;

    @JsonProperty("type")
    private String type;

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
