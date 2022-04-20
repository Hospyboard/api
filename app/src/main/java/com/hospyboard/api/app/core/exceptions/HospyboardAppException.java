package com.hospyboard.api.app.core.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
@Slf4j
public class HospyboardAppException extends RuntimeException {
    public HospyboardAppException(final String errorMessage) {
        super(errorMessage);
        log.error(errorMessage);
    }

    public HospyboardAppException(final String errorMessage, final Throwable throwable) {
        super(errorMessage, throwable);
        log.error(errorMessage, throwable);
    }
}
