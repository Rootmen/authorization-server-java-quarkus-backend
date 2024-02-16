package ru.iedt.authorization.exception;

public class AuthorizationException extends RuntimeException {

    String error;
    String message;
    Exception base;

    public AuthorizationException(String error, String message, Exception base) {
        this.error = error;
        this.message = message;
        this.base = base;
    }

    public AuthorizationException(String message, String error) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Exception getBase() {
        return base;
    }
}
