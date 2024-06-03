package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The artwork of an anime.
 */
@Data
public class AnilistArtwork implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @JsonProperty("type")
    private String type;
    
    @JsonProperty("img")
    private String image;
    
    @JsonProperty("providerId")
    private String providerId;

}
