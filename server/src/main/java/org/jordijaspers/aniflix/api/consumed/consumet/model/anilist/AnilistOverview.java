package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jordijaspers.aniflix.common.mappers.model.PageableApiResponse;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The search result of an anime.
 */
@Data
public class AnilistOverview implements PageableApiResponse, Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

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
}
