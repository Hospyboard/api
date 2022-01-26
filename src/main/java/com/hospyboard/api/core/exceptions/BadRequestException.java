package com.hospyboard.api.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends HospyboardAppException {
    public BadRequestException(String errorMessage) {
        super(errorMessage);
    }

    public BadRequestException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
