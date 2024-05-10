package org.jordijaspers.aniflix.api.recommendation.model.response;

import lombok.Data;

import java.time.ZonedDateTime;

/**
 * The response object for the recommendation.
 */
@Data
public class RecommendationResponse {

    private Integer anilistId;

    private Long malId;

    private String title;

    private String status;

    private long episodes;

    private String image;

    private String cover;

    private long rating;

    private String type;

    private boolean liked;

    private boolean inLibrary;

    private String watchStatus;

    private int lastSeenEpisode;

    private ZonedDateTime lastInteraction;

}
