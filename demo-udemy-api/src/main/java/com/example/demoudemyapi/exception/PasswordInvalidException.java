package com.example.demoudemyapi.exception;

public class PasswordInvalidException extends RuntimeException {
    public PasswordInvalidException(String senha) {
        super(senha);
    }
}
