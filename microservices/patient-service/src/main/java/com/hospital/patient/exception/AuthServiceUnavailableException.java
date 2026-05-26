package com.hospital.patient.exception;

public class AuthServiceUnavailableException extends RuntimeException {

    public AuthServiceUnavailableException(String message) {
        super(message);
    }

}
