package com.springboot.project.common.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.springboot.project.common.exception.BadRequestException;
import com.springboot.project.common.exception.ResourceNotFoundException;
import com.springboot.project.common.generated.model.ErrorResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

class GlobalExceptionHandlerTest {

    private static final String REQUEST_PATH = "uri=/api/private-app/transactions";

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void should_map_bad_request_exception_to_400_body() {
        // given
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn(REQUEST_PATH);

        // when
        ErrorResponseModel response =
                exceptionHandler.handleBadRequestException(
                        new BadRequestException("invalid input"), request);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getCode());
    }

    @Test
    void should_map_resource_not_found_exception_to_404_body() {
        // given
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn(REQUEST_PATH);

        // when
        ErrorResponseModel response =
                exceptionHandler.handleResourceNotFoundException(
                        new ResourceNotFoundException("missing"), request);

        // then
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getCode());
    }

    @Test
    void should_map_unexpected_exception_to_500_body() {
        // given
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn(REQUEST_PATH);

        // when
        ErrorResponseModel response =
                exceptionHandler.handleUnexpectedException(new IllegalStateException("boom"), request);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getCode());
    }
}
