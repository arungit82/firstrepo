package com.carnival.mm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by david on 7/25/16.
 */
@ResponseStatus(value= HttpStatus.CONFLICT, reason="Cannot update this medallion.")
public class MedallionCannotUpdateException extends RuntimeException {
    public MedallionCannotUpdateException(String message) {
        super(message);
    }
}
