package com.example.demoudemyapi.web.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString

public class MensagemErro {

    private String path;
    private String metodo;

    private int status;

    private String statusDescricao;

    private String mensagem;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> erros;

    public MensagemErro() {

    }

    public MensagemErro(HttpServletRequest request, HttpStatus status, String mensagem) {
        this.path = request.getRequestURI();
        this.metodo = request.getMethod();
        this.status = status.value();
        this.statusDescricao = status.getReasonPhrase();
        this.mensagem = mensagem;
    }

    public MensagemErro(HttpServletRequest request, HttpStatus status, String mensagem, BindingResult resultado) {
        this.path = request.getRequestURI();
        this.metodo = request.getMethod();
        this.status = status.value();
        this.statusDescricao = status.getReasonPhrase();
        this.mensagem = mensagem;
        addErro(resultado);
    }

    private void addErro(BindingResult resultado) {
        this.erros = new HashMap<>();
        for (FieldError erro : resultado.getFieldErrors()) {
            this.erros.put(erro.getField(), erro.getDefaultMessage());
        }
    }

}
