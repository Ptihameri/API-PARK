package com.example.demoudemyapi.exception;

public class UsernameEmailException extends RuntimeException {
    public UsernameEmailException(String mensagem) {
        super(mensagem);
    }
}
