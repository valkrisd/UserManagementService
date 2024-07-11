package com.niiazov.usermanagement.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


// Класс для обработки исключений в приложении с помощью аннотации @ControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {
    // Обработка исключения типа ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<UserErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        UserErrorResponse errorResponse = new UserErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Обработка исключения типа ResourceNotCreatedException
    @ExceptionHandler(ResourceNotCreatedException.class)
    public ResponseEntity<UserErrorResponse> handleResourceNotCreatedException(ResourceNotCreatedException ex) {
        UserErrorResponse errorResponse = new UserErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // Обработка исключения типа ResourceNotUpdatedException
    @ExceptionHandler(ResourceNotUpdatedException.class)
    public ResponseEntity<UserErrorResponse> handleResourceNotUpdatedException(ResourceNotUpdatedException ex) {
        UserErrorResponse errorResponse = new UserErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // Обработка всех оставшихся эксепшенов
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        UserErrorResponse errorResponse = new UserErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
