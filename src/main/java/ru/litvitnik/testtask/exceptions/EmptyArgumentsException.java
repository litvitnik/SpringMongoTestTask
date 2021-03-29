package ru.litvitnik.testtask.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason = "Nothing to process")
public class EmptyArgumentsException extends RuntimeException {
}
