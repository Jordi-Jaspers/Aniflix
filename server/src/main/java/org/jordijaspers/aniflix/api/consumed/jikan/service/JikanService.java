package org.jordijaspers.aniflix.api.consumed.jikan.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.consumed.jikan.repository.JikanRepository;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * A service to interact with the Jikan API.
 */
@Service
@RequiredArgsConstructor
public class JikanService {

    private final JikanRepository jikanRepository;

    public void provisionTrailer(final Anime anime) {
        if (isBlank(anime.getTrailerUrl())) {
            final String trailerUrl = jikanRepository.getAnimeTrailer(anime.getMalId()).getYoutubeId();
            anime.setTrailerUrl(trailerUrl);
        }
    }
}
