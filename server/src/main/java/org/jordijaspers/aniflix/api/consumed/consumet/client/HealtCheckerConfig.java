/*
 * Copyright (C) ilionx Group BV 2021
 */

package org.jordijaspers.aniflix.api.consumed.consumet.client;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The configuration for the HealthChecker WebClient.
 */
@Configuration
@RequiredArgsConstructor
public class HealtCheckerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealtCheckerConfig.class);

    /**
     * Creates a WebClient to check the health of random domains.
     */
    @Bean
    public WebClient healthCheckClient() {
        LOGGER.info("Building generic client for health checks");
        final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);

        // Setting the maxInMemorySize to -1 disables the default limit of 256KB.
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        return WebClient.builder()
                .exchangeStrategies(strategies)
                .uriBuilderFactory(factory)
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .defaultHeader(CONNECTION, "keep-alive")
                .build();
    }
}
