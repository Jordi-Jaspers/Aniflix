package org.jordijaspers.aniflix.config;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.constant.Genres;
import org.jordijaspers.aniflix.api.authentication.model.Authority;
import org.jordijaspers.aniflix.api.authentication.model.Role;
import org.jordijaspers.aniflix.api.authentication.repository.RoleRepository;
import org.jordijaspers.aniflix.api.genre.GenreRepository;
import org.jordijaspers.aniflix.api.genre.model.Genre;
import org.jordijaspers.aniflix.config.properties.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataProvisioningConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataProvisioningConfiguration.class);

    private final RoleRepository roleRepository;

    private final GenreRepository genreRepository;

    private final ApplicationProperties applicationProperties;

    @EventListener(ApplicationStartedEvent.class)
    public void loadData() {
        LOGGER.info("Application context for '{} ({})' has been started, provisioning data...",
                applicationProperties.getName(),
                applicationProperties.getVersion());

        Authority.stream().forEach(authority -> {
            if (!roleRepository.existsByAuthority(authority)) {
                LOGGER.debug("[Permissions] Provisioning role '{}' - '{}'", authority, authority.getDescription());
                roleRepository.save(new Role(authority));
            }
        });

        Genres.stream().forEach(genre -> {
            if (!genreRepository.existsByName(genre)) {
                LOGGER.debug("[Genres] Provisioning genre '{}'", genre.getName());
                genreRepository.save(new Genre(genre));
            }
        });

        LOGGER.info("Data provisioning completed successfully.");
    }
}
