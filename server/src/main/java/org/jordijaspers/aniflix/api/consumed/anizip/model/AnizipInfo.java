package org.jordijaspers.aniflix.api.consumed.anizip.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The response object containing the information of an anime information from ani.zip.
 */
@Data
public class AnizipInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnizipInfo.class);

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

    private void setEpisodes(final Map<String, Object> episodes) {
        final ObjectMapper objectMapper = new ObjectMapper();
        episodes.forEach((key, value) -> {
            try {
                Integer.parseInt(key);

                final AnizipEpisode anizipEpisode = objectMapper.convertValue(value, AnizipEpisode.class);
                LOGGER.info("########## Adding episode: {}", anizipEpisode);
                this.episodes.add(anizipEpisode);
            } catch (final NumberFormatException e) {
                LOGGER.warn("Episode key '{}' is not a number, skipping", key);
            }
        });
    }
}
