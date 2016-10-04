package com.carnival.mm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by d.jayaramaiah on 04-10-2016.
 */
@ResponseStatus(value= HttpStatus.CONFLICT, reason="Cannot update this order.")
public class GuestOrderCannotUpdateException extends RuntimeException {
    public GuestOrderCannotUpdateException(String message) {
        super(message);
    }
}
