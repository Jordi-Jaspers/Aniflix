package org.jordijaspers.aniflix.api.anime.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamingSourcesResponse {

    private String src;

    private String quality;

}
