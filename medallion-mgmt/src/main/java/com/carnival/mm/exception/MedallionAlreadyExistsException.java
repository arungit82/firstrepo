package com.carnival.mm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by david on 7/25/16.
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Medallion with HardwareId already exists.")
public class MedallionAlreadyExistsException extends RuntimeException {
    public MedallionAlreadyExistsException(String message) {
        super(message);
    }
}
