package org.jordijaspers.aniflix.api.news.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.news.model.NewsPost;
import org.jordijaspers.aniflix.api.news.model.mapper.NewsMapper;
import org.jordijaspers.aniflix.api.news.model.response.NewsPostResponse;
import org.jordijaspers.aniflix.api.news.service.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static org.jordijaspers.aniflix.api.Paths.NEWS_DETAILS;
import static org.jordijaspers.aniflix.api.Paths.NEWS_PATH;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The controller for the news endpoints.
 */
@RestController
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    private final NewsMapper newsMapper;

    @ResponseStatus(OK)
    @GetMapping(path = NEWS_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NewsPostResponse>> getNewsFeed() {
        final List<NewsPost> newsFeed = newsService.getNewsFeedUntil(LocalDateTime.now().minusWeeks(2));
        return ResponseEntity.status(OK).body(newsMapper.toNewsFeedResponses(newsFeed));
    }

    @ResponseStatus(OK)
    @GetMapping(path = NEWS_DETAILS, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NewsPostResponse> getNewsPost(@PathVariable("id") final Integer id) {
        final NewsPost post = newsService.getNewsPost(id);
        return ResponseEntity.status(OK).body(newsMapper.toNewsFeedResponse(post));
    }
}
