package org.jordijaspers.aniflix.api.recommendation.model.mapper;

import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistRecommendation;
import org.jordijaspers.aniflix.api.recommendation.model.response.RecommendationResponse;
import org.jordijaspers.aniflix.config.SharedMapperConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(config = SharedMapperConfiguration.class)
public abstract class RecommendationMapper {

    @Mapping(target = "anilistId", source = "id")
    @Mapping(target = "title", expression = "java(recommendation.getPreferredTitle())")
    @Named("toRecommendationResponse")
    public abstract RecommendationResponse toRecommendationResponse(AnilistRecommendation recommendation);

    @IterableMapping(qualifiedByName = "toRecommendationResponse")
    public abstract List<RecommendationResponse> toRecommendationResponses(List<AnilistRecommendation> recommendations);

}
