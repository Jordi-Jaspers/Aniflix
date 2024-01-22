package org.jordijaspers.aniflix.api.anime.repository;

import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Integer> {

    @Query("FROM Anime a "
            + "LEFT JOIN FETCH a.genres genres "
            + "LEFT JOIN FETCH a.episodes episodes "
            + "LEFT JOIN FETCH a.interactions interactions "
            + "LEFT JOIN FETCH episodes.streamingLinks links "
            + "WHERE SOUNDEX(a.title) = SOUNDEX(?1)")
    Optional<Anime> findByTitle(String title);

    @Query("FROM Anime a "
            + "LEFT JOIN FETCH a.genres genres "
            + "LEFT JOIN FETCH a.episodes episodes "
            + "LEFT JOIN FETCH a.interactions interactions "
            + "LEFT JOIN FETCH episodes.streamingLinks links "
            + "WHERE a.anilistId = ?1")
    Optional<Anime> findByAnilistId(int anilistId);
}
