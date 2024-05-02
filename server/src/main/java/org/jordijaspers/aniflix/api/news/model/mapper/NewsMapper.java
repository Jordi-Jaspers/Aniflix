package org.jordijaspers.aniflix.api.news.model.mapper;

import org.jordijaspers.aniflix.api.news.model.NewsPost;
import org.jordijaspers.aniflix.api.news.model.response.NewsPostResponse;
import org.jordijaspers.aniflix.config.SharedMapperConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(config = SharedMapperConfiguration.class)
public abstract class NewsMapper {

    @Named("toNewsFeedResponse")
    @Mapping(target = "topic", expression = "java(newsPost.getTopic().getGenre())")
    public abstract NewsPostResponse toNewsFeedResponse(NewsPost newsPost);

    @IterableMapping(qualifiedByName = "toNewsFeedResponse")
    public abstract List<NewsPostResponse> toNewsFeedResponses(List<NewsPost> newsPosts);

}
