package com.springboot.project.common.handler;

import com.springboot.project.common.exception.BadRequestException;
import com.springboot.project.common.generated.dto.ErrorResponseDto;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Bean-validation failures and unreadable bodies are answered with 400 by {@link
 * ResponseEntityExceptionHandler}; this class adds the application-specific mappings.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleBadRequestException(BadRequestException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleUnexpectedException(Exception ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    private static ErrorResponseDto buildErrorResponse(
            HttpStatus status, Exception ex, WebRequest request) {
        return new ErrorResponseDto()
                .code(status.value())
                .message(ex.getMessage())
                .timestamp(OffsetDateTime.now())
                .status(status.getReasonPhrase())
                .path(request.getDescription(false));
    }
}
