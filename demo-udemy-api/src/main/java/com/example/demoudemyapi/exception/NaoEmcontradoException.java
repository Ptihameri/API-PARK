package com.example.demoudemyapi.exception;

public class NaoEmcontradoException extends RuntimeException {
    public NaoEmcontradoException(String mensagem) {
        super(mensagem);
    }
}
