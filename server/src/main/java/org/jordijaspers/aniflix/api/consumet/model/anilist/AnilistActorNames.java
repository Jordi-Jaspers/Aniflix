package org.jordijaspers.aniflix.api.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
    private String _native;
    
    @JsonProperty("userPreferred")
    private String userPreferred;

}
