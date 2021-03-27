package ru.litvitnik.testtask.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Im pretty sure number can't look like this")
public class IncorrectPhoneNumberException extends RuntimeException {
}
