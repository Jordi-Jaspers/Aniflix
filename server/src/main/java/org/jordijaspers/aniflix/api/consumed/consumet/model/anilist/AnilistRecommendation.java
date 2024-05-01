package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static java.util.Objects.nonNull;

@Data
public class AnilistRecommendation {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("malId")
    private Long malId;

    @JsonProperty("title")
    private AnilistTitle title;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("episodes")
    private Long episodes;
    
    @JsonProperty("image")
    private String image;
    
    @JsonProperty("cover")
    private String cover;
    
    @JsonProperty("rating")
    private Long rating;
    
    @JsonProperty("type")
    private String type;

    /**
     * Returns the status of the anime in uppercase for mapping purposes.
     */
    public String getStatus() {
        return status.toUpperCase().replace(" ", "_");
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
