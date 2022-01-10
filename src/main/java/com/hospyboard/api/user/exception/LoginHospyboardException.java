package com.hospyboard.api.user.exception;

import com.hospyboard.api.core.exceptions.HospyboardAppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class LoginHospyboardException extends HospyboardAppException {
    private final static String ERROR_MESSAGE = "Your password and email does not match.";

    public LoginHospyboardException() {
        super(ERROR_MESSAGE);
    }

    public LoginHospyboardException(final Throwable throwable) {
        super(ERROR_MESSAGE, throwable);
    }
}
