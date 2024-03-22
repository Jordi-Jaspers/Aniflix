package org.jordijaspers.aniflix.api.interaction.model.response;

import lombok.Data;
import org.jordijaspers.aniflix.api.anime.model.constant.WatchStatus;
import org.jordijaspers.aniflix.api.anime.model.response.AnimeResponse;
import org.jordijaspers.aniflix.api.anime.model.response.DetailedAnimeResponse;

import java.time.ZonedDateTime;

@Data
public class InteractionResponse {

    private DetailedAnimeResponse anime;

    private WatchStatus watchStatus;

    private int lastSeenEpisode;

    private ZonedDateTime lastInteraction;

    private boolean liked;

    private boolean inLibrary;

    public <T extends AnimeResponse> void setAnime(final T anime) {
        this.anime = (DetailedAnimeResponse) anime;
    }

}
