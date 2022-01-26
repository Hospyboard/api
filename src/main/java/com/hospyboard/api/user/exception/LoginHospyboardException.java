package com.hospyboard.api.user.exception;

import com.hospyboard.api.core.exceptions.BadRequestException;

public class LoginHospyboardException extends BadRequestException {
    private final static String ERROR_MESSAGE = "Your password and username does not match.";

    public LoginHospyboardException() {
        super(ERROR_MESSAGE);
    }

    public LoginHospyboardException(final Throwable throwable) {
        super(ERROR_MESSAGE, throwable);
    }
}
