package org.jordijaspers.aniflix.api.consumed.consumet.model.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * The news feed of an anime.
 */
@Data
public class AnilistNewsFeed {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("uploadedAt")
    private LocalDateTime uploadedAt;

    @JsonProperty("topics")
    private List<String> topics;

    @JsonProperty("thumbnail")
    private String thumbnail;

    @JsonProperty("url")
    private String url;

    /**
     * Set the uploadedAt string which takes form as "MMM d, HH:mm" to a {@link LocalDateTime} with current year.
     *
     * @param uploadedAt The uploadedAt string.
     */
    public void setUploadedAt(final String uploadedAt) {
        final String cleanedUp = uploadedAt.replaceAll("\\s+", " ");
        final String date = cleanedUp.split(", ")[0] + ", " + LocalDateTime.now().getYear();
        final String time = cleanedUp.split(", ")[1];

        this.uploadedAt = LocalDateTime.of(
                LocalDate.parse(date, ofPattern("MMM d, yyyy")),
                LocalTime.parse(time, ofPattern("HH:mm"))
        );
    }
}
