package org.jordijaspers.aniflix.api.anime.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString(exclude = "episode")
@Table(name = "streaming_link")
public class StreamingLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "resolution")
    private String resolution;

    @Column(name = "link")
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    private Episode episode;

}
