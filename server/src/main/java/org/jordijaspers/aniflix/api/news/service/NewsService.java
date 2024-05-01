package org.jordijaspers.aniflix.api.news.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.news.model.NewsPost;
import org.jordijaspers.aniflix.api.news.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
}
