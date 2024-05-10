package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The names of an actor.
 */
@Data
@SuppressWarnings("PMD.FieldNamingConventions")
public class AnilistActorNames {

    @JsonProperty("first")
    private String first;
    
    @JsonProperty("last")
    private String last;
    
    @JsonProperty("full")
    private String full;
    
    @JsonProperty("native")
    private String nativeName;
    
    @JsonProperty("userPreferred")
    private String userPreferred;

}
