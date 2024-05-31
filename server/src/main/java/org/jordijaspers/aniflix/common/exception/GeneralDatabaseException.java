package org.jordijaspers.aniflix.common.exception;

import org.hawaiiframework.exception.ApiException;

import java.io.Serial;

import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.DATABASE_ERROR;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * An exception that is thrown when something goes wrong during a database operation.
 */
public class GeneralDatabaseException extends ApiException {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    public GeneralDatabaseException(final Throwable original, final String message) {
        super(DATABASE_ERROR, original, message);
    }

}
