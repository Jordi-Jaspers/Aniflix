package org.jordijaspers.aniflix.api.interaction.model.request;

import lombok.Data;

/**
 * A request model for user interactions.
 */
@Data
public class InteractionRequest {

    private int anilistId;

    private int lastSeenEpisode;

}
