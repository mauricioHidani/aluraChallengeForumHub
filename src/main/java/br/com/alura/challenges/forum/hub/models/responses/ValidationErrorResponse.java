package br.com.alura.challenges.forum.hub.models.responses;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public record ValidationErrorResponse(
        Integer statusCode,
        String status,
        Instant timestamp,
        String path,
        Map<String, String> errors
) {

    public static ValidationErrorResponse create(Integer statusCode, String status, Instant timestamp, String path,
                                                 MethodArgumentNotValidException exception) {
        return new ValidationErrorResponse(statusCode, status, timestamp, path, addErrors(exception));
    }

    public static Map<String, String> addErrors(MethodArgumentNotValidException e) {
        var errors = new HashMap<String, String>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(err -> errors.put(
                    err.getField(),
                    err.getDefaultMessage()
                ));
        return errors;
    }

}
