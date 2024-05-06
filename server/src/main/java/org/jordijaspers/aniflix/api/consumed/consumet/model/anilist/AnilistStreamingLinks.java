package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class AnilistStreamingLinks {

    @JsonProperty("headers")
    private Map<String, String> headers = new HashMap<>();

    @JsonProperty("sources")
    private List<AnilistSource> sources;

    @JsonProperty("download")
    private String downloadLink;

}
