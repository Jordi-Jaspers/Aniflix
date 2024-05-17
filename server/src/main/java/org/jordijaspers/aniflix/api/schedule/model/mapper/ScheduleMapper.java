package org.jordijaspers.aniflix.api.schedule.model.mapper;

import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistNextAiringEpisode;
import org.jordijaspers.aniflix.api.schedule.model.NextAiringEpisode;
import org.jordijaspers.aniflix.api.schedule.model.response.NextAiringEpisodeResponse;
import org.jordijaspers.aniflix.config.SharedMapperConfiguration;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.ZoneId;

import static java.util.Objects.isNull;
import static org.jordijaspers.aniflix.Aniflix.DEFAULT_TIMEZONE;

/**
 * The mapper for the recommendations.
 */
@Mapper(config = SharedMapperConfiguration.class)
public abstract class ScheduleMapper {

    /**
     * Converts an AnilistNextAiringEpisode to a NextAiringEpisode.
     */
    public NextAiringEpisode toNextAiringEpisode(final AnilistNextAiringEpisode anilistInfo) {
        final NextAiringEpisode nextAiringEpisode = new NextAiringEpisode();
        if (isNull(anilistInfo)) {
            return nextAiringEpisode;
        }

        final Instant airingTime = Instant.ofEpochMilli(anilistInfo.getAiringTime());
        nextAiringEpisode.setAiringTime(airingTime.atZone(ZoneId.of(DEFAULT_TIMEZONE)).toLocalDateTime());
        nextAiringEpisode.setTimeUntilAiring(anilistInfo.getTimeUntilAiring());
        nextAiringEpisode.setEpisode(anilistInfo.getEpisode());
        nextAiringEpisode.setHasNextEpisode(true);
        return nextAiringEpisode;
    }

    public abstract NextAiringEpisodeResponse toNextAiringEpisodeResponse(NextAiringEpisode nextAiringEpisode);
}
