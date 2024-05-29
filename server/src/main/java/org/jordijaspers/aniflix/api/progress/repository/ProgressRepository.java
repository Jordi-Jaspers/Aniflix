package org.jordijaspers.aniflix.api.progress.repository;

import org.jordijaspers.aniflix.api.progress.model.EpisodeProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository for the progress of the episodes.
 */
@Repository
public interface ProgressRepository extends JpaRepository<EpisodeProgress, Integer> {

}
