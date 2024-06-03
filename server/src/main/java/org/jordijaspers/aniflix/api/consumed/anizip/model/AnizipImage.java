package org.jordijaspers.aniflix.api.consumed.anizip.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * A response object containing the image model used in ani.zip.
 */
@Data
public class AnizipImage {

    @JsonProperty("coverType")
    private AnizipCoverType coverType;

    @JsonProperty("url")
    private String url;

    private void setCoverType(final String coverType) {
        this.coverType = AnizipCoverType.ofName(coverType);
    }
}
