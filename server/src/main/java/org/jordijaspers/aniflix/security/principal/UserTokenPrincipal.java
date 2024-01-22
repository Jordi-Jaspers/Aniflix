package org.jordijaspers.aniflix.security.principal;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jordijaspers.aniflix.api.authentication.model.User;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

/**
 * A Wrapper class to have a useful principal in the SecurityContext.
 */
@Data
@AllArgsConstructor
public class UserTokenPrincipal implements OAuth2Token {

    private User user;

    private Jwt jwt;

    @Override
    public String getTokenValue() {
        return jwt.getTokenValue();
    }

    @Override
    public Instant getIssuedAt() {
        return OAuth2Token.super.getIssuedAt();
    }

    @Override
    public Instant getExpiresAt() {
        return OAuth2Token.super.getExpiresAt();
    }
}
