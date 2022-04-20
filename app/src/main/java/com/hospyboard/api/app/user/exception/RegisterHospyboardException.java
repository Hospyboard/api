package com.hospyboard.api.app.user.exception;

import com.hospyboard.api.app.core.exceptions.BadRequestException;

public class RegisterHospyboardException extends BadRequestException {
    private final static String ERROR_MESSAGE = "Une erreur est survenue lors de la création de compte : ";

    public RegisterHospyboardException(final String message) {
        super(ERROR_MESSAGE + message);
    }
}
