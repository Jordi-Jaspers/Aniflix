package org.jordijaspers.aniflix.api.anime.service;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.repository.DataNotFoundException;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.anime.model.StreamingLinks;
import org.jordijaspers.aniflix.api.anime.model.constant.Genres;
import org.jordijaspers.aniflix.api.anime.repository.AnimeRepository;
import org.jordijaspers.aniflix.api.anime.repository.EpisodeRepository;
import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistRecentEpisode;
import org.jordijaspers.aniflix.api.consumed.consumet.service.ConsumetService;
import org.jordijaspers.aniflix.api.interaction.service.InteractionService;
import org.jordijaspers.aniflix.api.interaction.service.UserInteractionEnhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.ANIME_EPISODE_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
public class EpisodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpisodeService.class);

    private final EpisodeRepository episodeRepository;

    private final InteractionService interactionService;

    private final ConsumetService consumetService;

    public Episode getEpisodeOfAnime(final int anilistId, final int episodeNumber) {
        LOGGER.info("Retrieving episode '{}' of anime with anilist id '{}'", episodeNumber, anilistId);
        interactionService.getInteractedAnime(anilistId);
        return episodeRepository.findEpisodeByEpisodeAndAnilistId(anilistId, episodeNumber)
                .map(episode -> {
                    final StreamingLinks streamingLinks = consumetService.getStreamingsLinks(episode.getUrl());
                    episode.setStreamingLinks(streamingLinks);
                    return episode;
                })
                .orElseThrow(() -> new DataNotFoundException(ANIME_EPISODE_NOT_FOUND_ERROR));
    }
}
