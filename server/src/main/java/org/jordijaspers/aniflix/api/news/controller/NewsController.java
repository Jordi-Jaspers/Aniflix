package org.jordijaspers.aniflix.api.news.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.request.PageableRequest;
import org.jordijaspers.aniflix.api.news.model.NewsPost;
import org.jordijaspers.aniflix.api.news.model.mapper.NewsMapper;
import org.jordijaspers.aniflix.api.news.model.response.NewsPostResponse;
import org.jordijaspers.aniflix.api.news.service.NewsService;
import org.jordijaspers.aniflix.common.mappers.model.PageResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping(path = NEWS_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResource<NewsPostResponse>> getNewsFeed(@RequestBody final PageableRequest request) {
        final Page<NewsPost> newsFeed = newsService.getPaginatedNewsFeed(request);
        return ResponseEntity.status(OK).body(newsMapper.toPageResource(newsFeed));
    }

    @ResponseStatus(OK)
    @GetMapping(path = NEWS_DETAILS, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NewsPostResponse> getNewsPost(@PathVariable("id") final Integer id) {
        final NewsPost post = newsService.getNewsPost(id);
        return ResponseEntity.status(OK).body(newsMapper.toNewsFeedResponse(post));
    }
}
