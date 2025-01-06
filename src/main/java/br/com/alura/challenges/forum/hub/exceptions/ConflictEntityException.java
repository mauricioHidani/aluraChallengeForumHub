package br.com.alura.challenges.forum.hub.exceptions;

public class ConflictEntityException extends RuntimeException {

    public ConflictEntityException(String message) {
        super(message);
    }

    public ConflictEntityException(String message, Throwable cause) {
        super(message, cause);
    }

}
