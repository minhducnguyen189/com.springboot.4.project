package com.springboot.project.common.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.springboot.project.common.exception.BadRequestException;
import com.springboot.project.common.exception.ResourceNotFoundException;
import com.springboot.project.common.generated.dto.ErrorResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

class GlobalExceptionHandlerTest {

    private static final String REQUEST_PATH = "uri=/api/private-app/bank-accounts";

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void should_mapBadRequestException_to400Body() {
        // given
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn(REQUEST_PATH);

        // when
        ErrorResponseDto response =
                exceptionHandler.handleBadRequestException(
                        new BadRequestException("invalid input"), request);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void should_mapResourceNotFoundException_to404Body() {
        // given
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn(REQUEST_PATH);

        // when
        ErrorResponseDto response =
                exceptionHandler.handleResourceNotFoundException(
                        new ResourceNotFoundException("missing"), request);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_mapUnexpectedException_to500Body() {
        // given
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn(REQUEST_PATH);

        // when
        ErrorResponseDto response =
                exceptionHandler.handleUnexpectedException(new IllegalStateException("boom"), request);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
