package org.jordijaspers.aniflix.api.anime.model.request;

import lombok.Data;

/**
 * A request for a page of data.
 */
@Data
public class PageRequest {

    private int page;

    private int perPage;

}
