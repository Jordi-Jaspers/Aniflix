package org.jordijaspers.aniflix.api.anime.repository;

import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.common.util.logging.LogExecutionTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * The repository for the episodes.
 */
@Repository
@SuppressWarnings("PMD.MethodNamingConventions")
public interface EpisodeRepository extends JpaRepository<Episode, Integer> {

    @LogExecutionTime
    boolean existsByAnime_AnilistIdAndNumber(int anilistId, int number);

    @LogExecutionTime
    @Query("FROM Episode episode "
            + "LEFT JOIN FETCH episode.anime anime "
            + "LEFT JOIN FETCH episode.episodeProgresses episodeProgresses "
            + "LEFT JOIN FETCH episodeProgresses.user user "
            + "WHERE anime.anilistId = ?1 AND episode.number = ?2 ")
    Optional<Episode> findEpisodeByEpisodeAndAnilistId(int anilistId, int episodeNumber);

    @LogExecutionTime
    @Query("FROM Episode episode "
            + "LEFT JOIN FETCH episode.anime anime "
            + "LEFT JOIN FETCH episode.episodeProgresses episodeProgresses "
            + "LEFT JOIN FETCH episodeProgresses.user user "
            + "WHERE anime.anilistId = ?1 "
            + "ORDER BY episode.number")
    Set<Episode> findAllByAnilistId(Integer anilistId);
}
