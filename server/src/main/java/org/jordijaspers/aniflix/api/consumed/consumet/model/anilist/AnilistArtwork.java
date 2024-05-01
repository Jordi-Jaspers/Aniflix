package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AnilistArtwork {

    @JsonProperty("type")
    private String type;
    
    @JsonProperty("img")
    private String image;
    
    @JsonProperty("providerId")
    private String providerId;

}
