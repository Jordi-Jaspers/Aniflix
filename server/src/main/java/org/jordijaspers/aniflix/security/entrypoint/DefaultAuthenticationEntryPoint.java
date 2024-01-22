package org.jordijaspers.aniflix.security.entrypoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Authentication entry point returning a 401 instead of redirecting to a login page.
 * The redirect should be done by the FE application.
 */
@Component
public final class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationEntryPoint.class);

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException)
            throws IOException {
        LOGGER.debug("Authentication failed: '{}'.", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
