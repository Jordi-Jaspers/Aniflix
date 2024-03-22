package org.jordijaspers.aniflix.api.anime.model.response;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

import static org.jordijaspers.aniflix.api.consumet.ConsumetConstants.Constants.SLASH;

@Data
public class EpisodeResponse {

    private int anilistId;

    private String title;

    private String episodeTitle;

    private int episodeNumber;

    private String episodeUrl;

    private String description;

    private String image;

    private Set<StreamingLinkResponse> streamingLinks = new HashSet<>();

    private boolean liked;

    private boolean inLibrary;

    /**
     * Returns the urlId with a leading slash.
     */
    public String getEpisodeUrl() {
        return episodeUrl.startsWith(SLASH)
                ? episodeUrl
                : SLASH + episodeUrl;
    }

}
