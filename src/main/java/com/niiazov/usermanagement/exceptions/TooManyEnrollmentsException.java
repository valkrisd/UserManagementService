package com.niiazov.usermanagement.exceptions;

public class TooManyEnrollmentsException extends RuntimeException {
    public TooManyEnrollmentsException(String errorMsg) {
        super(errorMsg);
    }
}
