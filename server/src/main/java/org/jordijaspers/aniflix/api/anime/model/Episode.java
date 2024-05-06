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

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.jordijaspers.aniflix.api.consumed.consumet.ConsumetConstants.Constants.SLASH;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

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

    @Column(name = "url_id")
    private String url;

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
    private StreamingLinks streamingLinks;

    /**
     * Remove the slash in the beginning if present.
     */
    public String getUrl() {
        return url.startsWith(SLASH) ? url.substring(1) : url;
    }
}
