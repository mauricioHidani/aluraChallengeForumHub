package br.com.alura.challenges.forum.hub.controllers.advice;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.responses.ErrorResponse;
import br.com.alura.challenges.forum.hub.models.responses.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.time.Instant;

@RestControllerAdvice
public class AdviceExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                                   HttpServletRequest request) {
        final var status = HttpStatus.BAD_REQUEST;
        final var timestamp = Instant.now(Clock.systemUTC());
        final var result = ValidationErrorResponse.create(
                status.value(), status.name(), timestamp, request.getRequestURI(), e
        );

        return ResponseEntity.status(status).body(result);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException exception,
                                                                  HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException exception,
                                                           HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception, request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(final HttpStatus status,
                                                             final RuntimeException exception,
                                                             final HttpServletRequest request) {
        final var timestamp = Instant.now(Clock.systemUTC());
        final var result = new ErrorResponse(
            status.value(),
            status.name(),
            timestamp,
            exception.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(result);
    }

}
