package com.saviynt.identitybot.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String exception) {
        super(exception);
    }
}
