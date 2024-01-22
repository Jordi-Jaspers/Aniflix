package org.jordijaspers.aniflix.common.exception.handler;

import org.hawaiiframework.exception.ApiException;
import org.hawaiiframework.web.exception.ErrorResponseEnricher;
import org.hawaiiframework.web.resource.ApiErrorResponseResource;
import org.hawaiiframework.web.resource.ErrorResponseResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

/**
 * This enricher adds api error information to the error response resource.
 */
@Component
public class ApiErrorWithMessageResponseEnricher implements ErrorResponseEnricher {

    /**
     * {@inheritDoc}
     *
     * <p><strong>NOTE:</strong> This enricher only applies if throwable is an {@link ApiException} and
     * #errorResponseResource is an {@link ApiErrorResponseResource}.</p>
     */
    @Override
    public void doEnrich(
            final ErrorResponseResource errorResponseResource,
            final Throwable throwable,
            final WebRequest request,
            final HttpStatus httpStatus) {
        if (throwable instanceof final ApiException apiException
                && errorResponseResource instanceof final ApiErrorResponseResource responseResource) {
            responseResource.setApiErrorCode(apiException.getErrorCode());
            responseResource.setApiErrorReason(apiException.getReason());

            if (apiException.getMessage() != null) {
                responseResource.setErrorMessage(apiException.getMessage());
            } else {
                responseResource.setErrorMessage("No error message available");
            }
        }
    }
}
