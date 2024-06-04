package org.jordijaspers.aniflix.api.anime.model.mapper;

import org.jordijaspers.aniflix.api.anime.model.Episode;
import org.jordijaspers.aniflix.api.anime.model.response.EpisodeResponse;
import org.jordijaspers.aniflix.config.SharedMapperConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

/**
 * The mapper for the episodes.
 */
@Mapper(config = SharedMapperConfiguration.class, uses = StreamingLinkMapper.class)
public abstract class EpisodeMapper {

    @Named("toEpisodeResponse")
    @Mapping(target = "anilistId", source = "episode.anime.anilistId")
    @Mapping(target = "title", source = "episode.anime.title")
    @Mapping(target = "episodeTitle", source = "episode.title")
    @Mapping(target = "episodeNumber", source = "episode.number")
    @Mapping(target = "airDate", source = "episode.airDate")
    @Mapping(target = "duration", source = "episode.duration")
    @Mapping(target = "totalEpisodes", source = "episode.anime.totalEpisodes")
    @Mapping(target = "lastSeen", source = "episode.lastSeen")
    public abstract EpisodeResponse toEpisodeResponse(Episode episode);

    @IterableMapping(qualifiedByName = "toEpisodeResponse")
    public abstract List<EpisodeResponse> toEpisodeResponse(Set<Episode> episodes);

}
