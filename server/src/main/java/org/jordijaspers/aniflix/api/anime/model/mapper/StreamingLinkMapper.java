package org.jordijaspers.aniflix.api.anime.model.mapper;

import org.jordijaspers.aniflix.api.anime.model.StreamingLinks;
import org.jordijaspers.aniflix.api.anime.model.response.StreamingLinksResponse;
import org.jordijaspers.aniflix.api.anime.model.response.StreamingSourcesResponse;
import org.jordijaspers.aniflix.config.SharedMapperConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = SharedMapperConfiguration.class)
public abstract class StreamingLinkMapper {

    public abstract StreamingLinksResponse toStreamingLinkResponse(StreamingLinks link);

    public abstract StreamingSourcesResponse toStreamingSourcesResponse(StreamingLinks link);

    public abstract List<StreamingSourcesResponse> toStreamingSourcesResponseList(List<StreamingLinks> links);
}
