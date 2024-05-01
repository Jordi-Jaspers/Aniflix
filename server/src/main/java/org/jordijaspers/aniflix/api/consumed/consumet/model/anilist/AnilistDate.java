package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AnilistDate {

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
