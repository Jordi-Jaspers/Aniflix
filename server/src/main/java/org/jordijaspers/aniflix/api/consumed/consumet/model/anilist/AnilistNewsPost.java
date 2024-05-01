package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AnilistNewsPost {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("uploadedAt")
    private String uploadedAt;

    @JsonProperty("intro")
    private String intro;

    @JsonProperty("description")
    private String description;

    @JsonProperty("thumbnail")
    private String thumbnail;

    @JsonProperty("url")
    private String url;

}
