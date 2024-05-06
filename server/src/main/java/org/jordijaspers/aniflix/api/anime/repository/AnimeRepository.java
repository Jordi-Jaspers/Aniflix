package org.jordijaspers.aniflix.api.anime.repository;

import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.service.SynchronizeAnime;
import org.jordijaspers.aniflix.common.util.logging.LogExecutionTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Integer> {

    @SynchronizeAnime
    @LogExecutionTime
    @Query("FROM Anime a "
            + "LEFT JOIN FETCH a.genres genres "
            + "LEFT JOIN FETCH a.episodes episodes "
            + "LEFT JOIN FETCH a.interactions interactions "
            + "WHERE SOUNDEX(a.title) = SOUNDEX(?1)")
    Optional<Anime> findByTitle(String title);

    @SynchronizeAnime
    @LogExecutionTime
    @Query("FROM Anime a "
            + "LEFT JOIN FETCH a.genres genres "
            + "LEFT JOIN FETCH a.episodes episodes "
            + "LEFT JOIN FETCH a.interactions interactions "
            + "WHERE a.anilistId = ?1")
    Optional<Anime> findByAnilistId(int anilistId);
}
