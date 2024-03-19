package org.jordijaspers.aniflix.api.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

@Data
public class AnilistTitle implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @JsonProperty("romaji")
    private String romaji;

    @JsonProperty("english")
    private String english;

    @JsonProperty("native")
    private String japanse;

    @JsonProperty("userPreferred")
    private String userPreferred;

}
