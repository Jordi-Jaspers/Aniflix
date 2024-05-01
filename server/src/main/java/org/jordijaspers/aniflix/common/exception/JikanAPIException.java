package org.jordijaspers.aniflix.common.exception;

import org.hawaiiframework.exception.ApiException;

import java.io.Serial;

import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.JIKAN_API_ERROR;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * An exception that is thrown when something goes wrong while trying to communicate with the Jikan API.
 */
public class JikanAPIException extends ApiException {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    public JikanAPIException(final Throwable original) {
        super(JIKAN_API_ERROR, original);
    }

    public JikanAPIException(final String message) {
        super(JIKAN_API_ERROR, message);
    }

}
