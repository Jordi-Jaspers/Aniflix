package org.jordijaspers.aniflix.common.util;

import org.jordijaspers.aniflix.api.user.model.User;
import org.jordijaspers.aniflix.common.exception.AuthorizationException;
import org.jordijaspers.aniflix.security.principal.UserTokenPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.util.Objects.isNull;
import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.NO_USER_LOGGED_IN_ERROR;

/**
 * Utility class for security related operations.
 */
public final class SecurityUtil {

    private SecurityUtil() {
        // Utility class
    }

    /**
     * Retrieve the logged-in user from the security context.
     */
    public static User getLoggedInUser() {
        final UserTokenPrincipal principal = (UserTokenPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (isNull(principal) || isNull(principal.getUser())) {
            throw new AuthorizationException(NO_USER_LOGGED_IN_ERROR);
        }
        return principal.getUser();
    }
}
