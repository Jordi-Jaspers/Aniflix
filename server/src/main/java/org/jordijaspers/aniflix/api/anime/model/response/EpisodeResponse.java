package org.jordijaspers.aniflix.api.anime.model.response;

import lombok.Data;
import lombok.ToString;

import java.time.ZonedDateTime;

/**
 * The response for the episode.
 */
@Data
@ToString
public class EpisodeResponse {

    private int anilistId;

    private String title;

    private String episodeTitle;

    private int episodeNumber;

    private int totalEpisodes;

    private String summary;

    private String image;

    private ZonedDateTime airDate;

    private long duration;

    private StreamingLinksResponse streamingLinks;

    private int lastSeen;
}
