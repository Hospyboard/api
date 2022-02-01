package com.hospyboard.api.core.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @Getter
    @Setter
    private static class ResponseException {
        private String message;
        private Integer code;
    }

    @ExceptionHandler(HospyboardAppException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseException handleBaseException(HospyboardAppException e) {
        final ResponseException responseException = new ResponseException();

        responseException.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseException.setMessage(e.getMessage());
        return responseException;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException handleBaseException(BadRequestException e) {
        final ResponseException responseException = new ResponseException();

        responseException.setCode(HttpStatus.BAD_REQUEST.value());
        responseException.setMessage(e.getMessage());
        return responseException;
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseException handleBaseException(ForbiddenException e) {
        final ResponseException responseException = new ResponseException();

        responseException.setCode(HttpStatus.FORBIDDEN.value());
        responseException.setMessage(e.getMessage());
        return responseException;
    }
}
