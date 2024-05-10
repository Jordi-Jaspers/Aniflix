package org.jordijaspers.aniflix.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hawaiiframework.exception.ApiException;
import org.jordijaspers.aniflix.api.authentication.model.User;
import org.jordijaspers.aniflix.api.authentication.service.UserService;
import org.jordijaspers.aniflix.api.token.service.JwtService;
import org.jordijaspers.aniflix.api.token.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.startsWithIgnoreCase;

/**
 * The filter to extract the JWT token from the request and set the security context, when the token is validated against
 * the user and the expiration date. The filter is only applied once per request. When there is already an authentication
 * object in the security context, the filter is skipped.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private static final String BEARER = "bearer ";

    private final JwtService jwtService;

    private final TokenService tokenService;

    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException, ApiException {
        LOGGER.debug("Processing authentication for '{}'", request.getRequestURL());
        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (!startsWithIgnoreCase(authorizationHeader, BEARER)) {
            LOGGER.debug("No JWT token found in request headers.");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authorizationHeader.substring(BEARER.length());
        if (hasText(jwt)) {
            try {
                LOGGER.debug("Found JWT token in request headers: '{}'", jwt);
                final String email = jwtService.extractSubject(jwt);
                final User user = userService.loadUserByUsername(email);
                if (tokenService.isValidAccessToken(jwt, user)) {
                    LOGGER.debug("Authentication successful for '{}', setting security context.", user.getUsername());
                    SecurityContextHolder.getContext().setAuthentication(getAuthentication(user, request));
                    filterChain.doFilter(request, response);
                    return;
                }
            } catch (final ApiException exception) {
                filterChain.doFilter(request, response);
            }
        }

        LOGGER.debug("Authentication failed because of an invalid token.");
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(final User user, final HttpServletRequest request) {
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user,
                user.getRoles(),
                user.getAuthorities()
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }
}
