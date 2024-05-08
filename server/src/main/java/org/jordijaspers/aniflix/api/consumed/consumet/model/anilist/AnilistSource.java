package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import static org.jordijaspers.aniflix.api.consumed.consumet.ConsumetConstants.Constants.SLASH;

@Data
public class AnilistSource {

    @JsonProperty("url")
    private String url;

    @JsonProperty("isM3U8")
    private boolean isM3U8;

    @JsonProperty("quality")
    private String quality = "Backup";

}
