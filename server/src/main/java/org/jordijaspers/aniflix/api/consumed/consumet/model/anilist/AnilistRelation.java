package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The relation of an anime.
 */
@Data
public class AnilistRelation implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("relationType")
    private String relationType;
    
    @JsonProperty("malId")
    private Long malId;
    
    @Valid
    @JsonProperty("title")
    private AnilistTitle title;
    
    @JsonProperty("status")
    private String status;
   
    @JsonProperty("episodes")
    private Long episodes;
   
    @JsonProperty("image")
    private String image;
    
    @JsonProperty("color")
    private Object color;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("cover")
    private String cover;
    
    @JsonProperty("rating")
    private Long rating;

}
