package org.jordijaspers.aniflix.security.converter;

import lombok.Data;
import org.jordijaspers.aniflix.api.user.model.User;
import org.jordijaspers.aniflix.api.user.service.UserService;
import org.jordijaspers.aniflix.security.principal.JwtUserPrincipalAuthenticationToken;
import org.jordijaspers.aniflix.security.principal.UserTokenPrincipal;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Converts a {@link Jwt} into a {@link JwtUserPrincipalAuthenticationToken}.
 */
@Data
@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserService userService;

    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter;

    /**
     * Create a {@link JwtAuthenticationConverter} with the provided parameters.
     * Defaults grantedAuthoritiesConverter to {@link JwtGrantedAuthoritiesConverter}.
     */
    public JwtAuthenticationConverter(final UserService userService) {
        this.userService = userService;
        this.jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    }

    /**
     * Converts a {@link Jwt} into a {@link JwtUserPrincipalAuthenticationToken}.
     * Which basically wraps the {@link Jwt} and the {@link User} into a {@link UserTokenPrincipal}.
     * Meaning that the {@link Jwt} is the token and the {@link User} is the owner of that token.
     */
    @Override
    public JwtUserPrincipalAuthenticationToken convert(final Jwt jwt) {
        final User user = userService.loadUserByUsername(jwt.getSubject());
        final UserTokenPrincipal principal = new UserTokenPrincipal(user, jwt);
        final Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
        return new JwtUserPrincipalAuthenticationToken(principal, authorities);
    }
}
