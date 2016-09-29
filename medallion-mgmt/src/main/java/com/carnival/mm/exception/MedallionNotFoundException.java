package com.carnival.mm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by david on 7/25/16.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Medallion.")
public class MedallionNotFoundException extends RuntimeException {
    public MedallionNotFoundException(String message) {
        super(message);
    }
}
