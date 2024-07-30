package com.niiazov.usermanagement.exceptions;

public class ResourceNotUpdatedException extends RuntimeException {
    public ResourceNotUpdatedException(String errorMsg) {
        super(errorMsg);
    }
}
