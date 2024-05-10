package org.jordijaspers.aniflix.api.recommendation.model.mapper;

import org.jordijaspers.aniflix.api.consumed.consumet.model.anilist.AnilistRecommendation;
import org.jordijaspers.aniflix.api.recommendation.model.Recommendation;
import org.jordijaspers.aniflix.api.recommendation.model.response.RecommendationResponse;
import org.jordijaspers.aniflix.config.SharedMapperConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * The mapper for the recommendations.
 */
@Mapper(config = SharedMapperConfiguration.class)
public abstract class RecommendationMapper {

    @Mapping(target = "anilistId", source = "id")
    @Mapping(target = "title", expression = "java(recommendation.getTitle().getPreferredTitle())")
    @Named("toRecommendation")
    public abstract Recommendation toRecommendation(AnilistRecommendation recommendation);

    @IterableMapping(qualifiedByName = "toRecommendation")
    public abstract List<Recommendation> toRecommendations(List<AnilistRecommendation> recommendations);

    @Mapping(target = "watchStatus", expression = "java(recommendation.getWatchStatus().getValue())")
    @Named("toRecommendationResponse")
    public abstract RecommendationResponse toRecommendationResponse(Recommendation recommendation);

    @IterableMapping(qualifiedByName = "toRecommendationResponse")
    public abstract List<RecommendationResponse> toRecommendationsResponses(List<Recommendation> recommendations);

}
