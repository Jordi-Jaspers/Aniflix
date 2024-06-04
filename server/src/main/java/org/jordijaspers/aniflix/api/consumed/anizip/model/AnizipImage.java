package org.jordijaspers.aniflix.api.consumed.anizip.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * A response object containing the image model used in ani.zip.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnizipImage implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @JsonProperty("coverType")
    private AnizipCoverType coverType;

    @JsonProperty("url")
    private String url;

    private void setCoverType(final String coverType) {
        this.coverType = AnizipCoverType.ofName(coverType);
    }
}
