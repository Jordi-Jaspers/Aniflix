package org.jordijaspers.aniflix.api.anime.model.response;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class EpisodeResponse {

    private String urlId;

    private String title;

    private String description;

    private int number;

    private String image;

    private Set<StreamingLinkResponse> streamingLinks = new HashSet<>();

}
