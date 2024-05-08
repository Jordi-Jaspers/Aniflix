package org.jordijaspers.aniflix.api.anime.repository;

import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.common.util.logging.LogExecutionTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Integer> {

    @LogExecutionTime
    @Query("FROM Episode episode "
            + "LEFT JOIN FETCH episode.anime anime "
            + "WHERE anime.anilistId = ?1 AND episode.number = ?2")
    Optional<Episode> findEpisodeByEpisodeAndAnilistId(int anilistId, int episodeNumber);

    @LogExecutionTime
    Set<Episode> findAllByAnime_AnilistId(Integer anilistId);
}
