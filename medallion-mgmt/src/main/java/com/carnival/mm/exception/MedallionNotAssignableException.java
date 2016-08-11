package com.carnival.mm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by david on 8/11/16.
 */
@ResponseStatus(value= HttpStatus.CONFLICT, reason="Medallion cannot be assigned.")
public class MedallionNotAssignableException extends RuntimeException{
    public MedallionNotAssignableException(String message) {
        super(message);
    }

}
