package org.jordijaspers.aniflix.api.anime.model.response;

import lombok.Data;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.jordijaspers.aniflix.api.consumed.consumet.ConsumetConstants.Constants.SLASH;

@Data
@ToString
public class EpisodeResponse {

    private int anilistId;

    private String title;

    private String episodeTitle;

    private int episodeNumber;

    private String episodeId;

    private String image;

    private ZonedDateTime airDate;

    private long duration;

    private StreamingLinksResponse streamingLinks;
}
