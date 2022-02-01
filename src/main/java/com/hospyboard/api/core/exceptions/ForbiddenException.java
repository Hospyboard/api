package com.hospyboard.api.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends HospyboardAppException {
    private final static String PREFIX_MESSAGE = "Vous n'avez pas la permission. ";

    public ForbiddenException(String errorMessage) {
        super(PREFIX_MESSAGE + errorMessage);
    }

    public ForbiddenException(String errorMessage, Throwable throwable) {
        super(PREFIX_MESSAGE + errorMessage, throwable);
    }
}
