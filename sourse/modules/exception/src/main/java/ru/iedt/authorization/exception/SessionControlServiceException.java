package ru.iedt.authorization.exception;

public class SessionControlServiceException extends AuthorizationException {
    public SessionControlServiceException(String message, String error) {
        super(message, error);
    }
}
