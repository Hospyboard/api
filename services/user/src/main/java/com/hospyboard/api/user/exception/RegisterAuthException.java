package com.hospyboard.api.user.exception;

public class RegisterAuthException extends Exception {

    public RegisterAuthException(final String message) {
        super(message);
    }

    public RegisterAuthException(final String message, Throwable e) {
        super(message, e);
    }

}
