package org.jordijaspers.aniflix.common.exception;

import org.hawaiiframework.exception.ApiException;

import java.io.Serial;

import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.PASSWORD_DOES_NOT_MATCH;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * Exception thrown when the password does not match the current password.
 */
public class PasswordIncorrectException extends ApiException {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    public PasswordIncorrectException() {
        super(PASSWORD_DOES_NOT_MATCH);
    }

    public PasswordIncorrectException(final Exception original) {
        super(PASSWORD_DOES_NOT_MATCH, original);
    }
}
