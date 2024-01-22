package org.jordijaspers.aniflix.common.exception;

import org.hawaiiframework.exception.ApiException;

import java.io.Serial;

import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.INTERNAL_SERVER_ERROR;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * Placeholder exception for any uncaught exception within the application.
 */
public class InternalServerException extends ApiException {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    public InternalServerException() {
        super(INTERNAL_SERVER_ERROR);
    }

    public InternalServerException(final Throwable original) {
        super(INTERNAL_SERVER_ERROR, original);
    }
}
