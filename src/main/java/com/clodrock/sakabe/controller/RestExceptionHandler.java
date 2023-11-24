package com.clodrock.sakabe.controller;

import com.clodrock.sakabe.exception.InvalidAuthenticationException;
import com.clodrock.sakabe.exception.NotFoundException;
import com.clodrock.sakabe.model.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler {

    private final static String SYSTEM_ERROR = "Unexpected system error occurred!";

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ExceptionMessage> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ExceptionMessage.builder()
                        .error(true)
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionMessage> handleException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionMessage.builder()
                .error(true)
                .message(SYSTEM_ERROR)
                .build());
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ExceptionMessage> handleInvalidAuthenticationException(InvalidAuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ExceptionMessage.builder()
                        .error(true)
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionMessage> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> errors.add(error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(
                ExceptionMessage.builder()
                        .error(true)
                        .message(String.join(",", errors))
                        .build()
        );
    }
}