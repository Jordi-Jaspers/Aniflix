package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The source of an anime.
 */
@Data
public class AnilistSource implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @JsonProperty("url")
    private String url;

    @JsonProperty("isM3U8")
    private boolean isM3U8;

    @JsonProperty("quality")
    private String quality = "Backup";

}
