package org.jordijaspers.aniflix.api.anime.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.constant.AnimeSeason;
import org.jordijaspers.aniflix.api.anime.model.constant.AnimeStatus;
import org.jordijaspers.aniflix.api.anime.model.constant.Genres;
import org.jordijaspers.aniflix.api.anime.model.constant.MediaTypes;
import org.jordijaspers.aniflix.api.anime.model.constant.WatchStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.jordijaspers.aniflix.api.Paths.ANIME_CONSTANT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class ConstantController {

    @ResponseStatus(OK)
    @GetMapping(path = ANIME_CONSTANT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<?>>> getGenres() {
        final Map<String, List<?>> genres = Map.of(
                "genres", Genres.getAll(),
                "seasons", AnimeSeason.getAll(),
                "status", AnimeStatus.getAll(),
                "mediaTypes", MediaTypes.getAll(),
                "watchStatus", WatchStatus.getAll()
        );
        return ResponseEntity.status(OK).body(genres);
    }
}
