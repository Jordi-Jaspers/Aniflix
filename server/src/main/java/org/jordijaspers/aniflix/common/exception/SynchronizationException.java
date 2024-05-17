package org.jordijaspers.aniflix.common.exception;

import org.hawaiiframework.exception.ApiException;

import java.io.Serial;

import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.SYNCHRONIZATION_ERROR;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * Exception thrown when something goes wrong during the synchronization of the API.
 */
public class SynchronizationException extends ApiException {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    public SynchronizationException() {
        super(SYNCHRONIZATION_ERROR);
    }

    public SynchronizationException(final Exception original) {
        super(SYNCHRONIZATION_ERROR, original);
    }
}
