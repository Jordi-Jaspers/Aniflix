package org.jordijaspers.aniflix.api.news.repository;

import org.jordijaspers.aniflix.api.news.model.NewsPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The repository for the news posts.
 */
@Repository
public interface NewsRepository extends JpaRepository<NewsPost, Integer>, JpaSpecificationExecutor<NewsPost> {

    @Query("FROM NewsPost n WHERE n.uploadedAt >= :date ORDER BY n.uploadedAt DESC")
    List<NewsPost> findAllUntilSortedByUploadedAtDesc(@Param("date") LocalDateTime date);

    @Query(value = "SELECT uploaded_at FROM news_post ORDER BY uploaded_at DESC LIMIT 1", nativeQuery = true)
    Optional<LocalDateTime> findLatestUploadedAt();

}
