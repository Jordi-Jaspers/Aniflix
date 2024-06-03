package org.jordijaspers.aniflix.api.consumed.anizip.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.consumed.anizip.model.AnizipEpisode;
import org.jordijaspers.aniflix.api.consumed.anizip.model.AnizipInfo;
import org.jordijaspers.aniflix.api.consumed.anizip.repository.AnizipRepository;
import org.jordijaspers.aniflix.api.consumed.jikan.service.JikanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.logging.log4j.ThreadContext.isEmpty;

@Service
@RequiredArgsConstructor
public class AnizipService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnizipService.class);

    private final JikanService jikanService;

    private final AnizipRepository anizipRepository;

    public Anime applyAnimeInfo(final Anime anime) {
        final AnizipInfo anizipInfo = anizipRepository.getAnizipInfoByAniListId(anime.getAnilistId());
        applyAnimeInfo(anime, anizipInfo);
        return anime;
    }

    public Anime applyAnimeDetails(final Anime anime) {
        final AnizipInfo anizipInfo = anizipRepository.getAnizipInfoByAniListId(anime.getAnilistId());
        applyAnimeInfo(anime, anizipInfo);
        if (isNotEmpty(anime.getEpisodes())) {
            applyEpisodeInfo(anime, anizipInfo);
        } else {
            setEpisodeInfo(anime, anizipInfo);
        }
        return anime;
    }

    private static void applyEpisodeInfo(final Anime anime, final AnizipInfo anizipInfo) {
        final Set<Episode> episodes = anime.getEpisodes();

        episodes.forEach(episode -> {
            final AnizipEpisode source = anizipInfo.getEpisodes().stream()
                    .filter(anizipEpisode -> anizipEpisode.getEpisode() == episode.getNumber())
                    .findFirst()
                    .orElse(null);

            if (nonNull(source)) {
                episode.setAirDate(LocalDateTime.parse(source.getAirdate()));
                episode.setDuration(source.getLength());
                episode.setTitle(source.getTitle());
                episode.setSummary(source.getSummary());
            }
        });
    }

    private static void setEpisodeInfo(final Anime anime, final AnizipInfo anizipInfo) {
        final Set<Episode> episodes = anizipInfo.getEpisodes().stream()
                .map(anizipEpisode -> {
                    final Episode episode = new Episode();
                    episode.setAirDate(LocalDateTime.parse(anizipEpisode.getAirdate()));
                    episode.setDuration(anizipEpisode.getLength());
                    episode.setTitle(anizipEpisode.getTitle());
                    episode.setSummary(anizipEpisode.getSummary());
                    episode.setNumber(anizipEpisode.getEpisode());
                    episode.setAnime(anime);
                    return episode;
                })
                .collect(Collectors.toSet());

        anime.setEpisodes(episodes);
    }

    private void applyAnimeInfo(final Anime anime, final AnizipInfo anizipInfo) {
        jikanService.provisionTrailer(anime);
        anime.setTotalEpisodes(anizipInfo.getEpisodeCount());
        anizipInfo.getImages().forEach(anizipImage -> {
            switch (anizipImage.getCoverType()) {
                case BANNER -> anime.setCoverUrl(anizipImage.getUrl());
                case CLEARLOGO -> anime.setClearLogoUrl(anizipImage.getUrl());
                case FANART -> anime.setFanArtUrl(anizipImage.getUrl());
                case POSTER -> anime.setImageUrl(anizipImage.getUrl());
                default -> LOGGER.warn("Found an unknown cover type during mapping.");
            }
        });
    }
}
