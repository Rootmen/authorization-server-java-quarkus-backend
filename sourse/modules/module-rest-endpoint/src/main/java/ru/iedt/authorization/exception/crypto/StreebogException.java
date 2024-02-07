package ru.iedt.authorization.exception.crypto;

import ru.iedt.authorization.exception.AuthorizationException;

public class StreebogException extends AuthorizationException {

    public StreebogException(String message, String error) {
        super(message, error);
    }
}
