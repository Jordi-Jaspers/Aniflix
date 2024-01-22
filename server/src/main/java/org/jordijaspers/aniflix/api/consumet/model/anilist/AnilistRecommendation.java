package org.jordijaspers.aniflix.api.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AnilistRecommendation {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("malId")
    private Long malId;

    @JsonProperty("title")
    private AnilistTitle title;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("episodes")
    private Long episodes;
    
    @JsonProperty("image")
    private String image;
    
    @JsonProperty("cover")
    private String cover;
    
    @JsonProperty("rating")
    private Long rating;
    
    @JsonProperty("type")
    private String type;

}
