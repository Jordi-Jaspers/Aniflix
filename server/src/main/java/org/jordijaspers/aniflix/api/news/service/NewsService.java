package org.jordijaspers.aniflix.api.news.service;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.repository.DataNotFoundException;
import org.jordijaspers.aniflix.api.news.model.NewsPost;
import org.jordijaspers.aniflix.api.news.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.NEWS_POST_NOT_FOUND_ERROR;

/**
 * The service for the news posts.
 */
@Service
@RequiredArgsConstructor
public class NewsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsService.class);

    private final NewsRepository newsRepository;

    /**
     * Retrieve the news feed until the given date.
     *
     * @param localDateTime The date until the news feed should be retrieved.
     */
    public List<NewsPost> getNewsFeedUntil(final LocalDateTime localDateTime) {
        LOGGER.info("Retrieving news feed from '{}'", localDateTime);
        return newsRepository.findAllUntilSortedByUploadedAtDesc(localDateTime);
    }

    public NewsPost getNewsPost(final Integer id) {
        LOGGER.info("Retrieving news post with id '{}'", id);
        return newsRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(NEWS_POST_NOT_FOUND_ERROR));
    }
}
