package org.jordijaspers.aniflix.api.consumed.anizip.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The response object containing the information of an anime information from ani.zip.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnizipInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    private int anilistId;

    private int malId;

    @JsonProperty("episodeCount")
    private int episodeCount;

    @JsonProperty("episodes")
    private List<AnizipEpisode> episodes = new ArrayList<>();

    @JsonProperty("images")
    private List<AnizipImage> images = new ArrayList<>();

    private void setAnilistId(final Map<String, Object> mappings) {
        this.anilistId = (int) mappings.get("anilist_id");
    }

    private void setMalId(final Map<String, Object> mappings) {
        this.malId = (int) mappings.get("mal_id");
    }

    @SuppressWarnings("PMD.EmptyCatchBlock")
    private void setEpisodes(final Map<String, Object> episodes) {
        final ObjectMapper objectMapper = new ObjectMapper();
        episodes.forEach((key, value) -> {
            try {
                Integer.parseInt(key);
                final AnizipEpisode anizipEpisode = objectMapper.convertValue(value, AnizipEpisode.class);
                this.episodes.add(anizipEpisode);
            } catch (final NumberFormatException exception) {
                // Ignore the key if it is not a number
            }
        });
    }
}
