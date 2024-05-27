package org.jordijaspers.aniflix.api.anime.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * A request for a page of data.
 */
@Data
public class PageableRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    private int page;

    private int perPage;

}
