package org.jordijaspers.aniflix.api.consumed.consumet.model.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The common field in the different error responses in the Consumet API.
 */
@Data
public class ConsumetError {

    @JsonProperty("message")
    private final String message;

}
