package org.jordijaspers.aniflix.api.anime.model.request;

import lombok.Data;

@Data
public class PageRequest {

    private int page;

    private int perPage;

}
