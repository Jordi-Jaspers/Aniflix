package org.jordijaspers.aniflix.api.progress.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.user.model.User;

import java.io.Serial;
import java.io.Serializable;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The progress of an episode as a database entity.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "episode_progress")
@ToString(exclude = {"episode", "user"})
@EqualsAndHashCode(exclude = {"episode", "user"})
public class EpisodeProgress implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "last_seen")
    private int lastSeen;

    @ManyToOne(fetch = FetchType.LAZY)
    private Episode episode;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    /**
     * Constructor for the episode progress.
     */
    public EpisodeProgress(final int lastSeen, final Episode episode, final User user) {
        this.lastSeen = lastSeen;
        this.episode = episode;
        this.user = user;
    }
}
