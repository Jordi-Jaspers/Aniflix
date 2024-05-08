package org.jordijaspers.aniflix.common.exception;

import org.hawaiiframework.exception.ApiException;

import java.io.Serial;

import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.ANILIST_PROVIDERS_DOWN_ERROR;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * Exception thrown when the Anilist providers are down.
 */
public class AnilistProvidersDownException extends ApiException {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    public AnilistProvidersDownException() {
        super(ANILIST_PROVIDERS_DOWN_ERROR);
    }

    public AnilistProvidersDownException(final Exception original) {
        super(ANILIST_PROVIDERS_DOWN_ERROR, original);
    }
}
