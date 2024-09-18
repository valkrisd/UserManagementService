package com.niiazov.usermanagement.exceptions;

import com.niiazov.usermanagement.util.UserErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
        StringBuilder errorMessage = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errorMessage.append(fieldError.getField()).append(" - ").append(fieldError.getDefaultMessage()).append("; ");
        });
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<UserErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        UserErrorResponse errorResponse = new UserErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotCreatedException.class)
    public ResponseEntity<UserErrorResponse> handleResourceNotCreatedException(ResourceNotCreatedException ex) {
        UserErrorResponse errorResponse = new UserErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotUpdatedException.class)
    public ResponseEntity<UserErrorResponse> handleResourceNotUpdatedException(ResourceNotUpdatedException ex) {
        UserErrorResponse errorResponse = new UserErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(ActivationException.class)
    public ResponseEntity<UserErrorResponse> handleResourceNotFoundException(ActivationException ex) {
        UserErrorResponse errorResponse = new UserErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        UserErrorResponse errorResponse = new UserErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
