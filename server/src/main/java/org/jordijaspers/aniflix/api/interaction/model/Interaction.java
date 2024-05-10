package org.jordijaspers.aniflix.api.interaction.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.constant.WatchStatus;
import org.jordijaspers.aniflix.api.authentication.model.User;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import static org.jordijaspers.aniflix.api.anime.model.constant.WatchStatus.*;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The interaction entity.
 */
@Data
@Entity
@NoArgsConstructor
@ToString(exclude = {"user", "anime"})
@Table(name = "interaction")
public class Interaction implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Anime anime;

    @Enumerated(EnumType.STRING)
    @Column(name = "watch_status")
    private WatchStatus watchStatus = NOT_STARTED;

    @Column(name = "last_seen_episode")
    private int lastSeenEpisode;

    @Column(name = "liked")
    private boolean liked;

    @Column(name = "in_library")
    private boolean inLibrary;

    @UpdateTimestamp
    @Column(name = "last_interaction")
    private LocalDateTime lastInteraction;

    /**
     * Creates a new Interaction with an anime for a specific user.
     */
    public Interaction(final User user, final Anime anime) {
        this.user = user;
        this.anime = anime;
    }

    /**
     * Creates a new Interaction with an anilist id for a specific user.
     */
    public Interaction(final User user, final Integer anilistId) {
        this.user = user;
        this.anime = new Anime(anilistId);
    }

    /**
     * Retrieve the anilist id of the anime when available.
     */
    public int getAnilistId() {
        return anime.getAnilistId();
    }

    /**
     * Sets the last seen episode with the given episode number.
     */
    public void setLastSeenEpisode(final int episodeNumber) {
        if (episodeNumber <= anime.getTotalEpisodes()) {
            this.lastSeenEpisode = episodeNumber;
            if (this.lastSeenEpisode == anime.getTotalEpisodes()) {
                this.watchStatus = COMPLETED;
            } else if (lastSeenEpisode == 0) {
                this.watchStatus = NOT_STARTED;
            } else {
                this.watchStatus = WATCHING;
            }
        }
    }

    /**
     * Set the {@link Interaction}'s last seen episode depending on the incoming {@link WatchStatus}.
     */
    public void setWatchStatus(final WatchStatus watchStatus) {
        if (watchStatus == COMPLETED) {
            this.lastSeenEpisode = anime.getTotalEpisodes();
        } else if (watchStatus == NOT_STARTED) {
            this.lastSeenEpisode = 0;
        }
    }
}
