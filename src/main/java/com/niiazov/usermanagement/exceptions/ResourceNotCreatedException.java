package com.niiazov.usermanagement.exceptions;

public class ResourceNotCreatedException extends RuntimeException {
    public ResourceNotCreatedException(String errorMsg) {
        super(errorMsg);
    }
}
