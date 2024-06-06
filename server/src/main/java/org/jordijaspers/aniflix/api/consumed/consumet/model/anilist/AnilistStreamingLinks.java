package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The streaming links of an anime.
 */
@Data
public class AnilistStreamingLinks implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @JsonProperty("headers")
    private Map<String, String> headers = new ConcurrentHashMap<>();

    @JsonProperty("sources")
    private List<AnilistSource> sources;

    @JsonProperty("download")
    private String downloadLink;

}
