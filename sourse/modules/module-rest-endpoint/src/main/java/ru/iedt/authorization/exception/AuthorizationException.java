package ru.iedt.authorization.exception;

import org.jboss.logging.Logger;

public class AuthorizationException extends Exception {
    private static final Logger log = Logger.getLogger(AuthorizationException.class);
    String error;
    String message;
    Exception base;

    public AuthorizationException(String error, String message, Exception base) {
        this.error = error;
        this.message = message;
        this.base = base;
        log.error(base);
        log.error(message);
    }

    public AuthorizationException(String message, String error) {
        this.error = error;
        this.message = message;
        log.error(message);
    }
}
