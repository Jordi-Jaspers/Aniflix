package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The streaming links of an anime.
 */
@Data
public class AnilistStreamingLinks {

    @JsonProperty("headers")
    private Map<String, String> headers = new ConcurrentHashMap<>();

    @JsonProperty("sources")
    private List<AnilistSource> sources;

    @JsonProperty("download")
    private String downloadLink;

}
