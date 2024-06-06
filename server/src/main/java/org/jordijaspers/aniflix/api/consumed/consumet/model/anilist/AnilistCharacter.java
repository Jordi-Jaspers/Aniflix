package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The character of an anime.
 */
@Data
public class AnilistCharacter implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("role")
    private String role;
    
    @Valid
    @JsonProperty("name")
    private AnilistActorNames name;
    
    @JsonProperty("image")
    private String image;
    
    @Valid
    @JsonProperty("voiceActors")
    private List<AnilistVoiceActor> voiceActors = new ArrayList<>();

}
