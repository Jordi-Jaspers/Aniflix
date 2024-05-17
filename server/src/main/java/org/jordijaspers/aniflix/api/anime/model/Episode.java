package org.jordijaspers.aniflix.api.anime.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.jordijaspers.aniflix.api.consumed.consumet.ConsumetConstants.Constants.SLASH;
import static org.jordijaspers.aniflix.api.consumed.consumet.service.DomainHealthChecker.getActiveProvider;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The episode of an anime which represents an episode in the database.
 */
@Data
@Entity
@Table(name = "episode")
@ToString(exclude = "anime")
@EqualsAndHashCode(exclude = "anime")
public class Episode implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "gogoanime_id")
    private String gogoanimeId;

    @Column(name = "zoro_id")
    private String zoroId;

    @Column(name = "title")
    private String title;

    @Column(name = "number")
    private int number;

    @Column(name = "image")
    private String image;

    @Column(name = "duration")
    private long duration;

    @Column(name = "air_date")
    private LocalDateTime airDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Anime anime;

    @Transient
    private String episodeId;

    @Transient
    private StreamingLinks streamingLinks;

    /**
     * Remove the slash at the start in the gogoanime id, if it exists.
     */
    public String getGogoanimeId() {
        return nonNull(gogoanimeId) && gogoanimeId.startsWith(SLASH) ? gogoanimeId.substring(1) : gogoanimeId;
    }

    /**
     * Remove the slash at the start in the zoro id, if it exists.
     */
    public String getZoroId() {
        return nonNull(zoroId) && zoroId.startsWith(SLASH) ? zoroId.substring(1) : zoroId;
    }

    /**
     * @return the correct id of the episode based on the active provider.
     */
    public String getActiveEpisodeId() {
        return switch (getActiveProvider()) {
            case GOGOANIME -> getGogoanimeId();
            case ZORO -> getZoroId();
            default -> null;
        };
    }

    /**
     * Set the episode id to the correct provider.
     */
    public void setActiveEpisodeId(final AnilistProviders provider) {
        switch (provider) {
            case GOGOANIME -> setGogoanimeId(episodeId);
            case ZORO -> setZoroId(episodeId);
            default -> throw new IllegalArgumentException("The provider is not supported.");
        }
    }

    /**
     * Check if the episode is completed and contains all the necessary information.
     */
    public boolean isCompleted() {
        return nonNull(airDate)
                && isNotBlank(gogoanimeId)
                && isNotBlank(zoroId)
                && isNotBlank(title);
    }
}
