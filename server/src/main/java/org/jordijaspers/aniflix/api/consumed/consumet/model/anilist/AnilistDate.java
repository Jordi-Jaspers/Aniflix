package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The date of an anime.
 */
@Data
public class AnilistDate implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @JsonProperty("year")
    private Long year;
    
    @JsonProperty("month")
    private Long month;
    
    @JsonProperty("day")
    private Long day;
    
    public LocalDate toLocalDate() {
        return LocalDate.of(year.intValue(), month.intValue(), day.intValue());
    }
}
