/*
 * Copyright (C) ilionx Group BV 2021
 */

package org.jordijaspers.aniflix.api.consumet.client;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.config.properties.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static org.jordijaspers.aniflix.api.consumet.ConsumetConstants.Constants.SLASH;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@RequiredArgsConstructor
public class ConsumetConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumetConfig.class);

    private final ApplicationProperties properties;

    /**
     * Creates a WebClient for the Consumet API.
     *
     * @param baseUrl the base url of the Consumet API.
     */
    @Bean
    public WebClient consumetClient(@Value("${consumet.url}") final String baseUrl) {
        LOGGER.info("Building Consumet client with base url '{}'", baseUrl);
        final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);

        // Setting the maxInMemorySize to -1 disables the default limit of 256KB.
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        return WebClient.builder()
                .exchangeStrategies(strategies)
                .uriBuilderFactory(factory)
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .defaultHeader(USER_AGENT, properties.getName() + SLASH + properties.getVersion())
                .defaultHeader(CONNECTION, "keep-alive")
                .build();
    }
}
