package org.jordijaspers.aniflix.api.authentication.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.user.model.User;
import org.jordijaspers.aniflix.api.token.model.Token;
import org.jordijaspers.aniflix.api.token.service.TokenService;
import org.jordijaspers.aniflix.api.user.service.UserService;
import org.jordijaspers.aniflix.common.exception.AuthorizationException;
import org.jordijaspers.aniflix.common.exception.InvalidJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;
import static org.jordijaspers.aniflix.api.token.model.TokenType.*;
import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.AUTHORIZATION_ERROR;

/**
 * A service to manage authentication.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final TokenService tokenService;

    public User register(final User user, final String password) {
        LOGGER.info("Registering new user '{}'.", user.getUsername());
        return userService.registerAndNotify(user, password);
    }

    public User authorize(final String username, final String password) {
        authenticate(username, password);
        final User user = userService.loadUserByUsername(username);
        LOGGER.info("User '{}' successfully authenticated", username);
        return tokenService.generateAuthorizationTokens(user);
    }

    public User validate(final String token) {
        LOGGER.info("Attempting to validate user with token '{}'", token);
        User user = tokenService.findValidationTokenByValue(token).getUser();
        tokenService.invalidateTokensForUser(user, USER_VALIDATION_TOKEN);

        user.setValidated(true);
        user.setLastLogin(user.getLastLogin());
        user = userService.updateUserDetails(user);
        LOGGER.info("User '{}' successfully validated", user.getEmail());
        return tokenService.generateAuthorizationTokens(user);
    }

    public void resendValidation(final String email) {
        userService.resendValidationEmail(email);
    }

    public User refresh(final String refreshToken) {
        final Token token = tokenService.findByValue(refreshToken);
        if (nonNull(token)) {
            final User user = token.getUser();
            LOGGER.info("Refreshing tokens for user '{}'", user.getUsername());
            return tokenService.generateAuthorizationTokens(user);
        } else {
            throw new InvalidJwtException();
        }
    }

    public void logout(final User user) {
        if (nonNull(user)) {
            tokenService.invalidateTokensForUser(user, ACCESS_TOKEN, REFRESH_TOKEN);
            LOGGER.info("User '{}' logged out", user.getEmail());
        }
    }

    private void authenticate(final String username, final String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (final AuthenticationException exception) {
            LOGGER.error("Authorization failed for specified user '{}'", username);
            if (exception.getCause() instanceof AuthorizationException) {
                throw (AuthorizationException) exception.getCause();
            }
            throw new AuthorizationException(AUTHORIZATION_ERROR, exception);
        }
    }
}
