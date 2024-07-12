package com.niiazov.usermanagement.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * @author Vladislav Niiazov
 */
public class ErrorsUtil {
    public static String getErrorMessage(BindingResult bindingResult) {

        StringBuilder errorMsg = new StringBuilder();

        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errorMsg.append(error.getField())
                    .append(" - ").append(error.getDefaultMessage())
                    .append(("; "));
        }
        return errorMsg.toString();
    }
}
