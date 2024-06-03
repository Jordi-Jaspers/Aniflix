package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The recommendation of an anime.
 */
@Data
@EqualsAndHashCode(exclude = "status")
public class AnilistRecommendation implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("malId")
    private Long malId;

    @JsonProperty("title")
    private AnilistTitle title;

    @JsonProperty("status")
    private String status;

    @JsonProperty("episodes")
    private Long episodes;

    @JsonProperty("image")
    private String image;

    @JsonProperty("cover")
    private String cover;

    @JsonProperty("rating")
    private Long rating;

    @JsonProperty("type")
    private String type;

    /**
     * Returns the status of the anime in uppercase for mapping purposes.
     */
    public String getStatus() {
        return status.toUpperCase().replace(" ", "_");
    }
}
