package org.jordijaspers.aniflix.api.genre.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.constant.Genres;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "genre")
@ToString(exclude = "anime")
public class Genre implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", unique = true)
    private Genres name;

    @ManyToMany(mappedBy = "genres")
    private List<Anime> anime;

    public Genre(final Genres genres) {
        this.id = genres.getId();
        this.name = genres;
    }

    public String getNameAsString() {
        return name.getName();
    }
}
