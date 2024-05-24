package org.jordijaspers.aniflix.api.interaction.repository;

import org.jordijaspers.aniflix.api.user.model.User;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.jordijaspers.aniflix.common.util.logging.LogExecutionTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The repository for interactions.
 */
@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Integer> {

    @LogExecutionTime
    @Query("FROM Interaction i "
            + "LEFT JOIN FETCH i.anime anime "
            + "LEFT JOIN FETCH anime.genres genres "
            + "WHERE i.user = ?2 "
            + "AND anime.anilistId IN (?1)")
    List<Interaction> findAllByAnilistIdIn(List<Integer> anilistIds, User user);

    @LogExecutionTime
    @Query("FROM Interaction i "
            + "LEFT JOIN FETCH i.anime anime "
            + "LEFT JOIN FETCH anime.genres genres "
            + "LEFT JOIN FETCH anime.episodes episodes "
            + "LEFT JOIN FETCH anime.interactions interactions "
            + "WHERE i.user = ?2 "
            + "AND anime.anilistId = ?1")
    Optional<Interaction> findByAnilistId(Integer anilistId, User user);

    @LogExecutionTime
    @Query("FROM Interaction i "
            + "LEFT JOIN FETCH i.anime anime "
            + "LEFT JOIN FETCH anime.genres genres "
            + "WHERE i.user = ?2 "
            + "AND anime.anilistId = ?1")
    List<Interaction> findAllByUser(User user);

    @LogExecutionTime
    @Query("FROM Interaction i "
            + "LEFT JOIN FETCH i.anime anime "
            + "LEFT JOIN FETCH anime.genres genres "
            + "WHERE i.user = ?2 "
            + "AND anime.title LIKE %?1% "
            + "AND i.inLibrary = true "
            + "ORDER BY i.lastInteraction DESC")
    List<Interaction> searchLibraryByTitleForUser(String title, User user);

    @LogExecutionTime
    @Query("FROM Interaction i "
            + "LEFT JOIN FETCH i.anime anime "
            + "LEFT JOIN FETCH anime.genres genres "
            + "WHERE i.user = ?1 "
            + "AND i.inLibrary = true "
            + "ORDER BY i.lastInteraction DESC")
    List<Interaction> findAllLibraryForUser(User user);
}
