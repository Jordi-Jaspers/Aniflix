package org.jordijaspers.aniflix.common.exception;

import org.hawaiiframework.exception.ApiException;

import java.io.Serial;

import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.INVALID_TOKEN_ERROR;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * Exception thrown when parsing an invalid JWT.
 */
public class InvalidJwtException extends ApiException {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    public InvalidJwtException() {
        super(INVALID_TOKEN_ERROR);
    }

    public InvalidJwtException(final Exception original) {
        super(INVALID_TOKEN_ERROR, original);
    }
}
