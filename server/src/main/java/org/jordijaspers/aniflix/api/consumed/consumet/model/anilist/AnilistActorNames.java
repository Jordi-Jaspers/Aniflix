package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The names of an actor.
 */
@Data
@SuppressWarnings("PMD.FieldNamingConventions")
public class AnilistActorNames implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

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
