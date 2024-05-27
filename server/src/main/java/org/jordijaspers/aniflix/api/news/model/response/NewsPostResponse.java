package org.jordijaspers.aniflix.api.news.model.response;

import lombok.Data;
import org.jordijaspers.aniflix.common.mappers.model.PageableItemResource;

import java.time.ZonedDateTime;

/**
 * The response for the news posts.
 */
@Data
public class NewsPostResponse implements PageableItemResource {
    
    private Integer id;

    private String articleId;

    private String title;

    private ZonedDateTime uploadedAt;

    private String intro;

    private String description;

    private String topic;

    private String thumbnail;

    private String url;

}
