package org.jordijaspers.aniflix.config;

import org.jordijaspers.aniflix.common.mappers.DateTimeMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = DateTimeMapper.class)
public interface SharedMapperConfiguration {
    // Just an empty interface for the mapper configuration.
}
