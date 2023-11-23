package com.example.demoudemyapi.web.controller;

import com.example.demoudemyapi.jwt.JwtDetalhesDoUsuarioServico;
import com.example.demoudemyapi.jwt.JwtToken;
import com.example.demoudemyapi.web.dto.UsuarioLoginDTO;
import com.example.demoudemyapi.web.dto.UsuarioResponseDTO;
import com.example.demoudemyapi.web.exception.MensagemErro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticacao", description = "Recurso para Autenticacao de usuario")
@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin
public class AutenticacaoController {

    private final JwtDetalhesDoUsuarioServico datailsService;
    private final AuthenticationManager autenticationManager;

    @Operation(summary = "Autenticar usuario", description = "Recurso para fazer a autenticação do usuario",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario autenticado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Credenciais invalidas",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class))),
                    @ApiResponse(responseCode = "422", description = "Campo invalido",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class)))
            })
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
