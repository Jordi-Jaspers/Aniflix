package org.jordijaspers.aniflix.api.consumed.consumet.model.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConsumetError {

    @JsonProperty("message")
    private final String message;

}
