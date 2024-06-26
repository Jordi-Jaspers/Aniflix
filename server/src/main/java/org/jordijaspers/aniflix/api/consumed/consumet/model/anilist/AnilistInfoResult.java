package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jordijaspers.aniflix.common.mappers.model.PageableApiResponse;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The result of an anime info request.
 */
@Data
@EqualsAndHashCode(exclude = "status")
public class AnilistInfoResult implements PageableApiResponse, Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @JsonProperty("id")
    private String id;

    @JsonProperty("malId")
    private Long malId;

    @Valid
    @JsonProperty("title")
    private AnilistTitle title;

    @Valid
    @JsonProperty("synonyms")
    private List<String> synonyms = new ArrayList<>();

    @JsonProperty("isLicensed")
    private Boolean isLicensed;

    @JsonProperty("isAdult")
    private Boolean isAdult;

    @JsonProperty("countryOfOrigin")
    private String countryOfOrigin;

    @Valid
    @JsonProperty("trailer")
    private AnilistTrailer trailer;

    @JsonProperty("image")
    private String image;

    @JsonProperty("popularity")
    private Long popularity;

    @JsonProperty("color")
    private String color;

    @JsonProperty("cover")
    private String cover;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private String status;

    @JsonProperty("releaseDate")
    private Long releaseDate;

    @Valid
    @JsonProperty("startDate")
    private AnilistDate startDate;

    @Valid
    @JsonProperty("endDate")
    private AnilistDate endDate;

    @Valid
    @JsonProperty("nextAiringEpisode")
    private AnilistNextAiringEpisode nextAiringEpisode;

    @JsonProperty("totalEpisodes")
    private Long totalEpisodes;

    @JsonProperty("currentEpisode")
    private Long currentEpisode;

    @JsonProperty("rating")
    private Long rating;

    @JsonProperty("duration")
    private Long duration;

    @Valid
    @JsonProperty("genres")
    private List<String> genres = new ArrayList<>();

    @JsonProperty("season")
    private String season;

    @Valid
    @JsonProperty("studios")
    private List<String> studios = new ArrayList<>();

    @JsonProperty("subOrDub")
    private String subOrDub;

    @JsonProperty("type")
    private String type;

    @Valid
    @JsonProperty("recommendations")
    private List<AnilistRecommendation> recommendations = new ArrayList<>();

    @Valid
    @JsonProperty("characters")
    private List<AnilistCharacter> characters = new ArrayList<>();

    @Valid
    @JsonProperty("relations")
    private List<AnilistRelation> relations = new ArrayList<>();

    @Valid
    @JsonProperty("artwork")
    private List<AnilistArtwork> artwork = new ArrayList<>();

    @JsonProperty("episodes")
    @Valid
    private List<AnilistEpisode> episodes = new ArrayList<>();

    /**
     * Returns the status of the anime in uppercase for mapping purposes.
     */
    public String getStatus() {
        return status.toUpperCase().replace(" ", "_");
    }
}

