package org.jordijaspers.aniflix.api.consumed.anizip.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The response object containing the information of an anime information from ani.zip.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnizipEpisode implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @JsonProperty("episode")
    private int episode;

    @JsonProperty("anidbEid")
    private int anidbEid;

    @JsonProperty("length")
    private int duration;

    @JsonProperty("airdate")
    private String airdate;

    @JsonProperty("rating")
    private double rating;

    @JsonProperty("title")
    private String title;

    @JsonProperty("summary")
    private String summary;

    public void setTitle(final Map<String, String> title) {
        if (title.containsKey("en")) {
            this.title = title.get("en");
        } else if (title.containsKey("ja")) {
            this.title = title.get("ja");
        } else if (title.containsKey("x-jat")) {
            this.title = title.get("x-jat");
        } else {
            this.title = title.get(title.keySet().iterator().next());
        }
    }
}
