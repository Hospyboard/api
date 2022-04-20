package com.hospyboard.api.app.user.exception;

import com.hospyboard.api.app.core.exceptions.BadRequestException;

public class LoginHospyboardException extends BadRequestException {
    private final static String ERROR_MESSAGE = "Your password and username does not match.";

    public LoginHospyboardException() {
        super(ERROR_MESSAGE);
    }

    public LoginHospyboardException(final Throwable throwable) {
        super(ERROR_MESSAGE, throwable);
    }
}
