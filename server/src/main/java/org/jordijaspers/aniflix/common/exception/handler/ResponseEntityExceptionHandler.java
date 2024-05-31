package org.jordijaspers.aniflix.common.exception.handler;

import org.hawaiiframework.web.exception.ErrorResponseEntityBuilder;
import org.hawaiiframework.web.exception.HawaiiResponseEntityExceptionHandler;
import org.jordijaspers.aniflix.common.exception.GeneralDatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * This class creates proper HTTP response bodies for exceptions.
 *
 * @author Jordi Jaspers
 */
@Order
@ControllerAdvice
public class ResponseEntityExceptionHandler extends HawaiiResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseEntityExceptionHandler.class);

    /**
     * The constructor with a {@code errorResponseEntityBuilder}.
     */
    public ResponseEntityExceptionHandler(final ErrorResponseEntityBuilder errorResponseEntityBuilder) {
        super(errorResponseEntityBuilder);
    }

    /**
     * Handles {@code ValidationException} instances.
     *
     * <p>The response status is: 400 Bad Request.
     *
     * @param exception the exception
     * @param request   the current request
     * @return a response entity reflecting the current exception
     */
    @ResponseBody
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDatabaseExceptions(final DataAccessException exception, final WebRequest request) {
        LOGGER.error("Something went wrong during a database operation", exception);
        return handleApiException(new GeneralDatabaseException(exception, "Cannot execute database operation."), request);
    }
}
