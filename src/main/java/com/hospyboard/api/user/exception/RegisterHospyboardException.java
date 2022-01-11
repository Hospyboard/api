package com.hospyboard.api.user.exception;

import com.hospyboard.api.core.exceptions.HospyboardAppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RegisterHospyboardException extends HospyboardAppException {
    private final static String ERROR_MESSAGE = "Une erreur est survenue lors de la création de compte : ";

    public RegisterHospyboardException(final String message) {
        super(ERROR_MESSAGE + message);
    }
}
