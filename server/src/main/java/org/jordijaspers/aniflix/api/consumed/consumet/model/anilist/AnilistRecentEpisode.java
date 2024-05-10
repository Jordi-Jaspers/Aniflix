package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The recent episode of an anime.
 */
@Data
public class AnilistRecentEpisode implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @JsonProperty("id")
    private int anilistId;

    @JsonProperty("title")
    private AnilistTitle title;

    @JsonProperty("episodeTitle")
    private String episodeTitle;

    @JsonProperty("episodeNumber")
    private int episodeNumber;

    @JsonProperty("episodeId")
    private String episodeUrl;

    @JsonProperty("image")
    private String image;

}
