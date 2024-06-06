package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The voice actor of an anime.
 */
@Data
public class AnilistVoiceActor implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("language")
    private String language;

    @Valid
    @JsonProperty("name")
    private AnilistActorNames name;

    @JsonProperty("image")
    private String image;

}
