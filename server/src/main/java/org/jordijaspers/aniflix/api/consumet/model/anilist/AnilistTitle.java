package org.jordijaspers.aniflix.api.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AnilistTitle {

    @JsonProperty("romaji")
    private String romaji;

    @JsonProperty("english")
    private String english;

    @JsonProperty("native")
    private String japanse;

    @JsonProperty("userPreferred")
    private String userPreferred;
}
