package com.example.demoudemyapi.exception;

public class CodigoUniqueViolationException extends RuntimeException {
    public CodigoUniqueViolationException(String mensagem) {
        super(mensagem);

    }
}
