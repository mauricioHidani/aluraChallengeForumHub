package br.com.alura.challenges.forum.hub.exceptions;

public class UnauthorizedRequisitionException extends RuntimeException {

    public UnauthorizedRequisitionException(String message) {
        super(message);
    }

    public UnauthorizedRequisitionException(String message, Throwable cause) {
        super(message, cause);
    }

}
