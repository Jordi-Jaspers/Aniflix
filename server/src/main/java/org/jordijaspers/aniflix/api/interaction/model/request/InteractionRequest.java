package org.jordijaspers.aniflix.api.interaction.model.request;

import lombok.Data;

@Data
public class InteractionRequest {

    private int anilistId;

    private int lastSeenEpisode;

}
