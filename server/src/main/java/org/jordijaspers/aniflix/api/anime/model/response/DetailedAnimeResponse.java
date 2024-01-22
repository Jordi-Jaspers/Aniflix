package org.jordijaspers.aniflix.api.anime.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DetailedAnimeResponse extends AnimeResponse {

    private List<EpisodeResponse> episodes = new ArrayList<>();

}
