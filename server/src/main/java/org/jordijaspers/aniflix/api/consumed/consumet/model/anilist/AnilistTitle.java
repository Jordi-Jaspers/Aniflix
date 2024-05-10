package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static java.util.Objects.nonNull;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The title of an anime.
 */
@Data
public class AnilistTitle implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @JsonProperty("romaji")
    private String romaji;

    @JsonProperty("english")
    private String english;

    @JsonProperty("native")
    private String japanse;

    @JsonProperty("userPreferred")
    private String userPreferred;

    /**
     * Returns the title of the anime in the language you prefer.
     */
    public String getPreferredTitle() {
        final String preferredTitle;
        if (nonNull(this.english)) {
            preferredTitle = this.english;
        } else if (nonNull(this.romaji)) {
            preferredTitle = this.romaji;
        } else if (nonNull(this.japanse)) {
            preferredTitle = this.japanse;
        } else if (nonNull(this.userPreferred)) {
            preferredTitle = this.userPreferred;
        } else {
            preferredTitle = "";
        }
        return preferredTitle;
    }

}
