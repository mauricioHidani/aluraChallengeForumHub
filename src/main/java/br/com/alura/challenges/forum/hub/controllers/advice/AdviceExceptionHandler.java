package br.com.alura.challenges.forum.hub.controllers.advice;

import br.com.alura.challenges.forum.hub.exceptions.ConflictEntityException;
import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.exceptions.UnauthorizedRequisitionException;
import br.com.alura.challenges.forum.hub.models.responses.ErrorResponse;
import br.com.alura.challenges.forum.hub.models.responses.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
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

    @ExceptionHandler(ConflictEntityException.class)
    public ResponseEntity<ErrorResponse> conflictEntityException(ConflictEntityException exception,
                                                                 HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, exception, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialsException(BadCredentialsException exception,
                                                                 HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Credenciais inválidas",
                request
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationException(AuthenticationException exception,
                                                                 HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Falha na autenticação",
                request
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedException(AccessDeniedException exception,
                                                               HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.FORBIDDEN,
                "Acesso negado",
                request
        );
    }

    @ExceptionHandler(UnauthorizedRequisitionException.class)
    public ResponseEntity<ErrorResponse> unauthorizedRequisitionException(UnauthorizedRequisitionException exception,
                                                                  HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, exception, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception exception,
                                                   HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getLocalizedMessage(),
                request
        );
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

    private ResponseEntity<ErrorResponse> buildErrorResponse(final HttpStatus status,
                                                             final String exceptionMessage,
                                                             final HttpServletRequest request) {
        final var timestamp = Instant.now(Clock.systemUTC());
        final var result = new ErrorResponse(
                status.value(),
                status.name(),
                timestamp,
                exceptionMessage,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(result);
    }

}
