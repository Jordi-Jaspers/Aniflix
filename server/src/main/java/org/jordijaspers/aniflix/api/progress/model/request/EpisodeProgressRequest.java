package org.jordijaspers.aniflix.api.progress.model.request;

import lombok.Data;

/**
 * The request to update the progress of an episode.
 */
@Data
public class EpisodeProgressRequest {

    private int anilistId;

    private int episode;

    private int lastSeen;

}
