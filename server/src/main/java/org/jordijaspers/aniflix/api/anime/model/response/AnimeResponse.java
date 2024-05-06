package org.jordijaspers.aniflix.api.anime.model.response;

import jakarta.persistence.Transient;
import lombok.Data;
import org.jordijaspers.aniflix.api.anime.model.constant.WatchStatus;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class AnimeResponse {

    private int anilistId;

    private String title;

    private String description;

    private int totalEpisodes;

    private int rating;

    private String status;

    private String image;

    private String cover;

    private String trailer;

    private int releaseYear;

    private List<String> genres = new ArrayList<>();

    private String mediaType;

    private boolean subbed;

    private int likes;

    private boolean liked;

    private boolean inLibrary;

    private String watchStatus;

    private int lastSeenEpisode;

    private ZonedDateTime lastInteraction;

}
