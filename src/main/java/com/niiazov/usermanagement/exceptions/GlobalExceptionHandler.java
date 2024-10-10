package com.niiazov.usermanagement.exceptions;

import com.niiazov.usermanagement.util.UserManagementErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<UserManagementErrorResponse> createErrorResponse(Exception ex, HttpStatus status) {
        UserManagementErrorResponse errorResponse = new UserManagementErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(TooManyEnrollmentsException.class)
    public ResponseEntity<UserManagementErrorResponse> handleTooManyEnrollmentsException(TooManyEnrollmentsException ex) {
        return createErrorResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UserManagementErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<UserManagementErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return createErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotCreatedException.class)
    public ResponseEntity<UserManagementErrorResponse> handleResourceNotCreatedException(ResourceNotCreatedException ex) {
        return createErrorResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotUpdatedException.class)
    public ResponseEntity<UserManagementErrorResponse> handleResourceNotUpdatedException(ResourceNotUpdatedException ex) {
        return createErrorResponse(ex, HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(ActivationException.class)
    public ResponseEntity<UserManagementErrorResponse> handleActivationException(ActivationException ex) {
        return createErrorResponse(ex, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(EnrollmentAlreadyExistsException.class)
    public ResponseEntity<UserManagementErrorResponse> handleEnrollmentAlreadyExistsException(EnrollmentAlreadyExistsException ex) {
        return createErrorResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserManagementErrorResponse> handleGenericException(Exception ex) {
        return createErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
