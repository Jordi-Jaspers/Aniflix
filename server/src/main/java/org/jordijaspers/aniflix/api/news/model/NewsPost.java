package org.jordijaspers.aniflix.api.news.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jordijaspers.aniflix.common.mappers.model.PageableItem;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The entity for the news posts.
 */
@Data
@Entity
@Table(name = "news_post")
@EqualsAndHashCode(of = "id")
public class NewsPost implements Serializable, PageableItem {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "article_id")
    private String articleId;

    @Column(name = "title")
    private String title;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "topic")
    @Enumerated(EnumType.STRING)
    private NewsGenre topic;

    @Column(name = "intro")
    private String intro;

    @Column(name = "description")
    private String description;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "url")
    private String url;
}
