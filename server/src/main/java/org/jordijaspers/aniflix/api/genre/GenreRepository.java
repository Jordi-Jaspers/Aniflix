package org.jordijaspers.aniflix.api.genre;

import org.jordijaspers.aniflix.api.anime.model.constant.Genres;
import org.jordijaspers.aniflix.api.genre.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {

    boolean existsByName(@NonNull Genres name);

}
