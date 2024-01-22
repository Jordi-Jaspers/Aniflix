package org.jordijaspers.aniflix.common.exception.handler;

import org.hawaiiframework.web.exception.ErrorResponseEnricher;
import org.hawaiiframework.web.resource.ErrorResponseResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

/**
 * This enricher copies the error message from the exception onto the error response resource.
 */
@Component
public class HttpErrorWithMessageResponseEnricher implements ErrorResponseEnricher {

    /**
     * {@inheritDoc}
     */
    @Override
    public void doEnrich(
            final ErrorResponseResource errorResponseResource,
            final Throwable throwable,
            final WebRequest request,
            final HttpStatus httpStatus) {
        final String message = throwable.getMessage();
        if (StringUtils.hasText(message)) {
            errorResponseResource.setErrorMessage(message);
        }
    }
}
