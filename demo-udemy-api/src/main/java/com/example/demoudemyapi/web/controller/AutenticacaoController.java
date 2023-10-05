package com.example.demoudemyapi.web.controller;

import com.example.demoudemyapi.jwt.JwtDetalhesDoUsuarioServico;
import com.example.demoudemyapi.jwt.JwtToken;
import com.example.demoudemyapi.web.dto.UsuarioLoginDTO;
import com.example.demoudemyapi.web.exception.MensagemErro;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final JwtDetalhesDoUsuarioServico datailsService;
    private final AuthenticationManager autenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioLoginDTO dto, HttpServletRequest request) {
        log.info("Autenticando usuario: {}", dto.getUsername());
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

            autenticationManager.authenticate(authenticationToken);

            JwtToken token = datailsService.getJwtTokenAcesso(dto.getUsername());

            return ResponseEntity.ok(token);

        } catch (AuthenticationException ex) {
            log.error("Erro de autenticação par o usuario: {}", ex.getMessage());
        }
        return ResponseEntity
                .badRequest()
                .body(new MensagemErro(request, HttpStatus.BAD_REQUEST, "Credenciais invalidas a001"));
    }
}
