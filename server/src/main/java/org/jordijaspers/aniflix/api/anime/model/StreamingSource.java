package org.jordijaspers.aniflix.api.anime.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The streaming source model.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamingSource implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    private String src;

    private String quality;

}
