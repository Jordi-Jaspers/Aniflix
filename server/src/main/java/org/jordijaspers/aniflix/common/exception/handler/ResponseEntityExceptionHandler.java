/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jordijaspers.aniflix.common.exception.handler;

import org.hawaiiframework.web.exception.ErrorResponseEntityBuilder;
import org.hawaiiframework.web.exception.HawaiiResponseEntityExceptionHandler;
import org.hawaiiframework.web.resource.ErrorResponseResource;
import org.jordijaspers.aniflix.common.exception.InternalServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpHeaders.EMPTY;

/**
 * A custom exception handler for the application.
 */
@Primary
@ControllerAdvice
@Component("hawaiiResponseEntityExceptionHandler")
public class ResponseEntityExceptionHandler extends HawaiiResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseEntityExceptionHandler.class);

    private final ErrorResponseEntityBuilder errorResponseEntityBuilder;

    public ResponseEntityExceptionHandler(final ErrorResponseEntityBuilder errorResponseEntityBuilder) {
        super(errorResponseEntityBuilder);
        this.errorResponseEntityBuilder = errorResponseEntityBuilder;
    }

    /**
     * Handles {@code Throwable} instances. This method acts as a fallback handler.
     *
     * @param throwable the exception
     * @param request   the current request
     * @return a response entity reflecting the current exception
     */
    @Override
    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleThrowable(final Throwable throwable, final WebRequest request) {
        LOGGER.error("Oh dear, an unhandled exception. What should we do now..", throwable);
        return handleInternalTeapotException(new InternalServerException(throwable), request);
    }

    /**
     * Handles {@code InternalServerException} instances.
     */
    @ResponseBody
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<Object> handleInternalTeapotException(final InternalServerException exception, final WebRequest request) {
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(exception, buildErrorResponseBody(exception, status, request), EMPTY, status, request);
    }

    private ErrorResponseResource buildErrorResponseBody(final Throwable throwable, final HttpStatus status, final WebRequest request) {
        return errorResponseEntityBuilder.buildErrorResponseBody(throwable, status, request);
    }
}
