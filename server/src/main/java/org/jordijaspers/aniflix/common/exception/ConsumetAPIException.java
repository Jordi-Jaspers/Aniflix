package org.jordijaspers.aniflix.common.exception;

import org.hawaiiframework.exception.ApiException;

import java.io.Serial;

import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.CONSUMET_API_ERROR;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * An exception that is thrown when something goes wrong while trying to communicate with the Consumet API.
 */
public class ConsumetAPIException extends ApiException {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    public ConsumetAPIException(final Throwable original) {
        super(CONSUMET_API_ERROR, original);
    }

    public ConsumetAPIException(final String message) {
        super(CONSUMET_API_ERROR, message);
    }

}
