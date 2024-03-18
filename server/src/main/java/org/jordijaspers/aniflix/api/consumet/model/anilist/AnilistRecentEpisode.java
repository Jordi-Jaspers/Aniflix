package org.jordijaspers.aniflix.api.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static java.util.Objects.nonNull;
import static org.jordijaspers.aniflix.api.consumet.ConsumetConstants.Constants.SLASH;

@Data
public class AnilistRecentEpisode {

    @JsonProperty("id")
    private int anilistId;

    @JsonProperty("title")
    private AnilistTitle title;

    @JsonProperty("episodeTitle")
    private String episodeTitle;

    @JsonProperty("episodeNumber")
    private int episodeNumber;

    @JsonProperty("episodeId")
    private String episodeUrl;

    @JsonProperty("image")
    private String image;

    /**
     * Returns the urlId with a leading slash.
     */
    public String getEpisodeUrl() {
        return episodeUrl.startsWith(SLASH)
                ? episodeUrl
                : SLASH + episodeUrl;
    }

    /**
     * Returns the title of the anime in the language you prefer.
     */
    public String getPreferredTitle() {
        if (nonNull(title.getEnglish())) {
            return title.getEnglish().toLowerCase();
        } else if (nonNull(title.getRomaji())) {
            return title.getRomaji().toLowerCase();
        } else {
            return title.getJapanse().toLowerCase();
        }
    }
}
