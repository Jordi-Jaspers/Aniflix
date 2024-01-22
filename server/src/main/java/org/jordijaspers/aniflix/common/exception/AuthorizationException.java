package org.jordijaspers.aniflix.common.exception;

import org.hawaiiframework.exception.ApiException;

import java.io.Serial;

import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.AUTHORIZATION_ERROR;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * An exception that indicates that the authentication failed, because of invalid credentials
 * or because the user is not enabled/validated.
 */
public class AuthorizationException extends ApiException {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    public AuthorizationException(final ApiErrorCode errorCode) {
        super(errorCode);
    }

    public AuthorizationException() {
        super(AUTHORIZATION_ERROR);
    }

    public AuthorizationException(final ApiErrorCode errorCode, final Throwable original) {
        super(errorCode, original);
    }
}
