package org.jordijaspers.aniflix.common.exception;

import org.hawaiiframework.exception.ApiException;

import java.io.Serial;

import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.USER_ALREADY_EXISTS_ERROR;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * Exception thrown when a user already exists.
 */
public class UserAlreadyExistsException extends ApiException {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    public UserAlreadyExistsException() {
        super(USER_ALREADY_EXISTS_ERROR);
    }

    public UserAlreadyExistsException(final Exception original) {
        super(USER_ALREADY_EXISTS_ERROR, original);
    }
}
