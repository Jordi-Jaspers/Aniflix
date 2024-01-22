package org.jordijaspers.aniflix.api.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AnilistTrailer {

    @JsonProperty("id")
    private String id;

    @JsonProperty("site")
    private String site;

    @JsonProperty("thumbnail")
    private String thumbnail;

}
