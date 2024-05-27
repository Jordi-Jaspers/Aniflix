package org.jordijaspers.aniflix.common.exception;

import org.hawaiiframework.exception.ApiException;

import java.io.Serial;

import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.EMAIL_ALREADY_EXISTS_ERROR;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * Exception thrown when an email already exists.
 */
public class EmailAlreadyExistsException extends ApiException {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    public EmailAlreadyExistsException() {
        super(EMAIL_ALREADY_EXISTS_ERROR);
    }

    public EmailAlreadyExistsException(final Exception original) {
        super(EMAIL_ALREADY_EXISTS_ERROR, original);
    }
}
