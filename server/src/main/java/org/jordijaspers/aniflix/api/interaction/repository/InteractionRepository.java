package org.jordijaspers.aniflix.api.interaction.repository;

import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.authentication.model.User;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Integer> {

    @Query("FROM Interaction i "
            + "LEFT JOIN FETCH i.anime anime "
            + "LEFT JOIN FETCH anime.genres genres "
            + "WHERE i.user = ?2 "
            + "AND anime IN (?1)")
    List<Interaction> findAllByAnilistIdIn(List<Anime> anime, User user);

    @Query("FROM Interaction i "
            + "LEFT JOIN FETCH i.anime anime "
            + "LEFT JOIN FETCH anime.genres genres "
            + "LEFT JOIN FETCH anime.episodes episodes "
            + "LEFT JOIN FETCH anime.interactions interactions "
            + "LEFT JOIN FETCH episodes.streamingLinks links "
            + "WHERE i.user = ?2 "
            + "AND anime = ?1")
    Optional<Interaction> findByAnilistId(Anime anime, User user);

    @Query("FROM Interaction i "
            + "LEFT JOIN FETCH i.anime anime "
            + "LEFT JOIN FETCH anime.genres genres "
            + "WHERE i.user = ?2 "
            + "AND anime.anilistId = ?1")
    List<Interaction> findAllByUser(User user);

    @Query("FROM Interaction i "
            + "LEFT JOIN FETCH i.anime anime "
            + "LEFT JOIN FETCH anime.genres genres "
            + "WHERE i.user = ?2 "
            + "AND anime.title LIKE %?1% "
            + "AND i.inLibrary = true "
            + "ORDER BY i.lastInteraction DESC")
    List<Interaction> searchLibraryByTitleForUser(String title, User user);

    @Query("FROM Interaction i "
            + "LEFT JOIN FETCH i.anime anime "
            + "LEFT JOIN FETCH anime.genres genres "
            + "WHERE i.user = ?1 "
            + "AND i.inLibrary = true "
            + "ORDER BY i.lastInteraction DESC")
    List<Interaction> findAllLibraryForUser(User user);

}
