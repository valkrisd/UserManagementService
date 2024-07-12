package com.niiazov.usermanagement.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Утилитарный класс для формирования JSON-ответа при ошибках валидации
 */
@AllArgsConstructor
@Getter
@Setter
public class UserErrorResponse {

    private String message;
    private long timestamp;
}
