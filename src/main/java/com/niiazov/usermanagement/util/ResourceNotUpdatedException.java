package com.niiazov.usermanagement.util;

public class ResourceNotUpdatedException extends RuntimeException {
    public ResourceNotUpdatedException(String errorMsg) {
        super(errorMsg);
    }
}
