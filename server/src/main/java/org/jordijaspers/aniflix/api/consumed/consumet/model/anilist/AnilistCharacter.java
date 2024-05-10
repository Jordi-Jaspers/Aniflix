package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * The character of an anime.
 */
@Data
public class AnilistCharacter {

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
