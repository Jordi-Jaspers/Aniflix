package org.jordijaspers.aniflix.common.util;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.repository.DataNotFoundException;
import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders;
import org.jordijaspers.aniflix.api.consumed.consumet.service.ConsumetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders.GOGOANIME;
import static org.jordijaspers.aniflix.api.consumed.consumet.model.AnilistProviders.ZORO;

/**
 * Estimates the next episode id based on the given episodes ids.
 */
@Component
@RequiredArgsConstructor
public final class EpisodeEstimator {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpisodeEstimator.class);

    private static final String GOGOANIME_REGEX = "episode-(\\d+)";
    private static final String GOGOANIME_REPLACEMENT_FORMAT = "episode-%d";

    private static final String ZOROANIME_REGEX = "\\$episode\\$(\\d+)\\$sub";
    private static final String ZOROANIME_REPLACEMENT_FORMAT = "\\$episode\\$%d\\$";

    private final ConsumetService consumetService;

    public String estimateGogoAnimeId(final Set<Episode> episodes) {
        return estimateNextEpisodeId(episodes, GOGOANIME, GOGOANIME_REGEX, GOGOANIME_REPLACEMENT_FORMAT);
    }

    public String estimateZoroAnimeId(final Set<Episode> episodes) {
        return estimateNextEpisodeId(episodes, ZORO, ZOROANIME_REGEX, ZOROANIME_REPLACEMENT_FORMAT);
    }

    private String estimateNextEpisodeId(final Set<Episode> episodes, final AnilistProviders provider,
                                         final String regex, final String format) {
        final String exampleId = episodes.stream()
                .map(GOGOANIME.equals(provider) ? Episode::getGogoanimeId : Episode::getZoroId)
                .filter(id -> !isBlank(id))
                .findFirst()
                .orElse(null);

        String estimation = null;
        if (!isBlank(exampleId)) {
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(exampleId);

            if (matcher.find()) {
                final int currentEpisode = Integer.parseInt(matcher.group(1));
                final int nextEpisode = currentEpisode + 1;
                estimation = matcher.replaceAll(String.format(format, nextEpisode));
            }
        }

        try {
            consumetService.getStreamingsLinks(estimation, provider.getProvider());
            return estimation;
        } catch (final DataNotFoundException exception) {
            LOGGER.warn("Episode ID '{}' estimation was incorrect for provider: {}", estimation, provider);
            return null;
        }
    }
}
