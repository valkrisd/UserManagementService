package com.niiazov.usermanagement.exceptions;

public class EnrollmentAlreadyExistsException extends RuntimeException {
    public EnrollmentAlreadyExistsException(String errorMsg) {
        super(errorMsg);
    }
}
