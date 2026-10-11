package com.springboot.project.common.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.springboot.project.common.exception.BadRequestException;
import com.springboot.project.common.generated.dto.ErrorResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

class GlobalExceptionHandlerTest {

    private static final String REQUEST_PATH = "uri=/v1/drools/business-rules/actions/test";

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void should_map_bad_request_exception_to_400_body() {
        // given
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn(REQUEST_PATH);

        // when
        ErrorResponseDto response =
                exceptionHandler.handleBadRequestException(
                        new BadRequestException("Business rule does not compile"), request);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getMessage()).isEqualTo("Business rule does not compile");
        assertThat(response.getPath()).isEqualTo(REQUEST_PATH);
    }

    @Test
    void should_map_unexpected_exception_to_500_body() {
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
