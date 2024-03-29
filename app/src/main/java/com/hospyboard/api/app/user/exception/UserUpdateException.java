package com.hospyboard.api.app.user.exception;

import com.hospyboard.api.app.core.exceptions.BadRequestException;

public class UserUpdateException extends BadRequestException {
    public UserUpdateException(final String message) {
        super(message);
    }
    public UserUpdateException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
