package org.jordijaspers.aniflix.api.anime.model.response;


import lombok.Data;
import lombok.ToString;
import org.jordijaspers.aniflix.api.anime.model.StreamingSource;

import java.util.List;

/**
 * The response for the streaming links.
 */
@Data
@ToString
public class StreamingLinksResponse {

    private String referer;

    private List<StreamingSource> sources;

}
