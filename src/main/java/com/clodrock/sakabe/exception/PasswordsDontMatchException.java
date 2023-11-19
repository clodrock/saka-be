package com.clodrock.sakabe.exception;

public class PasswordsDontMatchException extends RuntimeException{
    public PasswordsDontMatchException(String message) {
        super(message);
    }
}
