package io.quell.techtest.error;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ErrorControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ErrorResponse> runtimeException(RuntimeException re, HttpServletRequest request) {
        return buildErrorResponseEntity(request, HttpStatus.BAD_REQUEST, re);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> exception(Exception e, HttpServletRequest request) {
        return buildErrorResponseEntity(request, HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponseEntity(HttpServletRequest request, HttpStatus httpStatus, Exception e) {
        log.error("Exception caught by the Error Controller", e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(httpStatus.value())
                .path(request.getRequestURI())
                .message(httpStatus.getReasonPhrase())
                .build();

        return ResponseEntity
                .status(httpStatus)
                .body(errorResponse);
    }
}
