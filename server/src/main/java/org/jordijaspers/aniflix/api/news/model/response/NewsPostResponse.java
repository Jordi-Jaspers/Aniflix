package org.jordijaspers.aniflix.api.news.model.response;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class NewsPostResponse {

    private String articleId;

    private String title;

    private ZonedDateTime uploadedAt;

    private String intro;

    private String description;

    private String thumbnail;

    private String url;

}
