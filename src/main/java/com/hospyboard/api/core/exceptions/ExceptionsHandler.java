package com.hospyboard.api.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(HospyboardAppException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public HospyboardAppException handleBaseException(HospyboardAppException e) {
        return e;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestException handleBaseException(BadRequestException e) {
        return e;
    }
}
