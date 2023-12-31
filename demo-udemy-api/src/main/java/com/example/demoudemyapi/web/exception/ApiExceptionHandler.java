package com.example.demoudemyapi.web.exception;

import com.example.demoudemyapi.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MensagemErro> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, BindingResult result) {
        log.error("Api erro:", ex);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).
                contentType(MediaType.APPLICATION_JSON)
                .body(new MensagemErro(request, HttpStatus.UNPROCESSABLE_ENTITY, "Erro de validação", result));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensagemErro> outroErroException(Exception ex, HttpServletRequest request) {
        MensagemErro erro = new MensagemErro(
                request,
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        log.error("Api erro interno: {} {}",erro, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR).
                contentType(MediaType.APPLICATION_JSON)
                .body(erro);
    }

    @ExceptionHandler({UsernameEmailException.class, CpfJaCadastradorException.class, CodigoUniqueViolationException.class})
    public ResponseEntity<MensagemErro> unicViolationException(RuntimeException ex, HttpServletRequest request) {
        log.error("Api erro:", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MensagemErro(request, HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(NaoEmcontradoException.class)
    public ResponseEntity<MensagemErro> naoEmcontradoException(RuntimeException ex, HttpServletRequest request) {
        log.error("Api erro:", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MensagemErro(request, HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<MensagemErro> passwordInvalidException(RuntimeException ex, HttpServletRequest request) {
        log.error("Api erro:", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MensagemErro(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MensagemErro> accessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        log.error("Api erro:", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MensagemErro(request, HttpStatus.FORBIDDEN, ex.getMessage()));
    }
}
