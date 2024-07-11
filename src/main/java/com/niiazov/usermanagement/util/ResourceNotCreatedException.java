package com.niiazov.usermanagement.util;

public class ResourceNotCreatedException extends RuntimeException {
    public ResourceNotCreatedException(String errorMsg) {
        super(errorMsg);
    }
}
