package org.jordijaspers.aniflix.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.jordijaspers.aniflix.security.converter.JwtAuthenticationConverter;
import org.jordijaspers.aniflix.security.entrypoint.DefaultAuthenticationEntryPoint;
import org.jordijaspers.aniflix.security.filter.JwtAuthenticationFilter;
import org.jordijaspers.aniflix.security.rsa.config.RSAKeyProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.jordijaspers.aniflix.api.Paths.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Configures spring web security.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfiguration implements WebMvcConfigurer {

    @Bean
    @Primary
    public PasswordEncoder encoder(@Value("${application.security.encoder.id:bcrypt}") final String encoderId) {
        final Map<String, PasswordEncoder> encoders = new ConcurrentHashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder(10));
        return new DelegatingPasswordEncoder(encoderId, encoders);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(final UserDetailsService userDetailsService, final PasswordEncoder encoder) {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(encoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtDecoder jwtDecoder(final RSAKeyProperties keys) {
        return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(final RSAKeyProperties keys) {
        final JWK jwk = new RSAKey.Builder(keys.getPublicKey())
                .privateKey(keys.getPrivateKey())
                .build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }

    @Bean
    public SecurityFilterChain filterChain(@Value("${application.security.cors.allowed-origins}") final String[] allowedOrigins,
                                           final DefaultAuthenticationEntryPoint authenticationEntryPoint,
                                           final JwtAuthenticationFilter jwtAuthenticationFilter,
                                           final JwtAuthenticationConverter jwtAuthenticationConverter,
                                           final AuthenticationProvider authenticationProvider,
                                           final HttpSecurity http) throws Exception {
        // Adding a once per request filter to check the JWT token.
        http.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Adding a custom authentication provider.
        http.authenticationProvider(authenticationProvider);

        // Disable CSRF because of state-less session-management.
        http.csrf(AbstractHttpConfigurer::disable);

        // Enable CORS.
        http.cors(cors -> cors.configurationSource(corsConfigurationSource(allowedOrigins)));

        // Configure Session Management.
        http.sessionManagement(sessionConfiguration -> sessionConfiguration.sessionCreationPolicy(STATELESS));

        // Configure OAuth2 Resource Server.
        http.oauth2ResourceServer(oAuth2Configurer -> {
            oAuth2Configurer.jwt(jwtConfigurer -> {
                final JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
                converter.setAuthorityPrefix("");
                converter.setAuthoritiesClaimName("roles");
                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
                jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter);
            });
        });

        // Add Exception Handling for unauthorized requests.
        http.exceptionHandling(handler -> handler.authenticationEntryPoint(authenticationEntryPoint));

        // Configure Endpoints
        http.authorizeHttpRequests(accessManagement -> {
            accessManagement.requestMatchers(PUBLIC_ACTUATOR_PATH + WILDCARD_PART).permitAll();
            accessManagement.requestMatchers(PUBLIC_PATH + WILDCARD_PART).permitAll();
            accessManagement.requestMatchers(AUTH_PATH + WILDCARD_PART).permitAll();
            accessManagement.requestMatchers(OPENAPI_PATH + WILDCARD_PART).permitAll();
            accessManagement.requestMatchers(ERROR_PATH).permitAll();

            accessManagement.requestMatchers(LOGOUT_PATH).authenticated();
            accessManagement.anyRequest().authenticated();
        });

        return http.build();
    }

    private UrlBasedCorsConfigurationSource corsConfigurationSource(final String... origins) {
        final var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(origins));
        configuration.setAllowedMethods(List.of(WILDCARD));
        configuration.setAllowedHeaders(List.of(WILDCARD));
        configuration.setExposedHeaders(List.of(WILDCARD));

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(WILDCARD_PART, configuration);
        return source;
    }
}
