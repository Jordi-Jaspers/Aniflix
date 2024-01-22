package org.jordijaspers.aniflix.api.anime.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;
import org.jordijaspers.aniflix.api.anime.model.constant.AnimeStatus;
import org.jordijaspers.aniflix.api.anime.model.constant.MediaTypes;
import org.jordijaspers.aniflix.api.anime.model.constant.WatchStatus;
import org.jordijaspers.aniflix.api.genre.model.Genre;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static io.micrometer.common.util.StringUtils.isNotBlank;
import static org.jordijaspers.aniflix.api.anime.model.constant.AnimeStatus.COMPLETED;

@Data
@Entity
@ToString
@NoArgsConstructor
@Table(name = "anime")
@EqualsAndHashCode(of = "anilistId")
public class Anime {

    @Id
    @Column(name = "anilist_id")
    private Integer anilistId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "total_episodes")
    private int totalEpisodes;

    @Column(name = "rating")
    private int rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AnimeStatus status = AnimeStatus.UNKNOWN;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "cover_url")
    private String coverUrl;

    @Column(name = "release_year")
    private int releaseYear;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "anime_genre",
            joinColumns = @JoinColumn(name = "anime_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @Column(name = "media_type")
    private MediaTypes mediaType = MediaTypes.UNKNOWN;

    @Column(name = "subbed")
    private boolean subbed;

    @OrderBy("number ASC")
    @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Episode> episodes = new HashSet<>();

    @UpdateTimestamp
    @Column(name = "updated")
    private LocalDateTime updated;

    @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Interaction> interactions = new HashSet<>();

    @Transient
    private WatchStatus watchStatus;

    /**
     * Constructor for JPA.
     */
    public Anime(final int anilistId) {
        this.anilistId = anilistId;
    }

    /**
     * Indicates if the anime is completed and fully updated with descriptions. If the anime is not complete but is
     * recently updated (15 minutes) we consider it complete for now.
     */
    public boolean isCompleted() {
        final boolean hasDescriptions = episodes.stream().allMatch(episode -> isNotBlank(episode.getDescription()));
        return status.equals(COMPLETED)
                && episodes.size() == totalEpisodes
                && hasDescriptions
                || updated.isAfter(LocalDateTime.now().minusMinutes(15));


    }

    /**
     * Calculates the amount of likes for this anime.
     */
    public int getLikes() {
        return interactions.stream()
                .filter(Interaction::isLiked)
                .toList()
                .size();
    }
}
