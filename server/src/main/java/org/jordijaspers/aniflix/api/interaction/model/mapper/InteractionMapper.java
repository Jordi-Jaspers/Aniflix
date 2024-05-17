package org.jordijaspers.aniflix.api.interaction.model.mapper;

import org.jordijaspers.aniflix.api.anime.model.mapper.AnimeMapper;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.jordijaspers.aniflix.api.interaction.model.request.InteractionRequest;
import org.jordijaspers.aniflix.api.interaction.model.response.InteractionResponse;
import org.jordijaspers.aniflix.config.SharedMapperConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * The mapper for interactions.
 */
@Mapper(config = SharedMapperConfiguration.class)
public abstract class InteractionMapper {

    @Autowired
    protected AnimeMapper animeMapper;

    @Named("toDetailedResponse")
    @Mapping(target = "anime", expression = "java(animeMapper.toResponseWithEpisodes(interaction.getAnime()))")
    public abstract InteractionResponse toDetailedResponse(Interaction interaction);

    @Named("toBasicResponse")
    @Mapping(target = "anime", expression = "java(animeMapper.toResourceObject(interaction.getAnime()))")
    public abstract InteractionResponse toBasicResponse(Interaction interaction);

    @IterableMapping(qualifiedByName = "toBasicResponse")
    public abstract List<InteractionResponse> toBasicResponse(List<Interaction> interactions);

    public abstract Interaction toDomain(InteractionRequest request);
}
