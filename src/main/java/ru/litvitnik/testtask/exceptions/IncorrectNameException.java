package ru.litvitnik.testtask.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason = "Name is too long")
public class IncorrectNameException extends RuntimeException{
}
