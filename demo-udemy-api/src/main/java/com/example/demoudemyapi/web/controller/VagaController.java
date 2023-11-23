package com.example.demoudemyapi.web.controller;

import com.example.demoudemyapi.entity.Vaga;
import com.example.demoudemyapi.service.VagaService;
import com.example.demoudemyapi.web.dto.VagaCreateDTO;
import com.example.demoudemyapi.web.dto.VagaResponseDTO;
import com.example.demoudemyapi.web.dto.mapper.VagaMapper;
import com.example.demoudemyapi.web.exception.MensagemErro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Vagas", description = "Contem operações de Vagas")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/vagas")
@CrossOrigin
public class VagaController {

    private final VagaService vagaService;

    @Operation(summary = "Criar uma vaga", description = "Recurso para criar uma vaga" + "Recursos de acesso: ADMIN",
            security = @SecurityRequirement(name = "Authorization"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Vaga criada com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL da vaga criada")),
                    @ApiResponse(responseCode = "403", description = "Sem permição para criar a vaga no sistema",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = MensagemErro.class))),
                    @ApiResponse(responseCode = "409", description = "Vaga codigo ja cadastrodo no sistema",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = MensagemErro.class))),
                    @ApiResponse(responseCode = "422", description = "Dados enviados invalidos ou incompletos",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = MensagemErro.class)))
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid VagaCreateDTO dto) {
        Vaga vaga = VagaMapper.toVaga(dto);
        vagaService.salvar(vaga);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(vaga.getCodigo()).toUri();
        return ResponseEntity.created(location).build();

    }

    @Operation(summary = "Buscar uma vaga pelo codigo", description = "Recurso para buscar uma vaga" + "Recursos de acesso: ADMIN",
            security = @SecurityRequirement(name = "Authorization"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vaga encontrada com sucesso",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = VagaResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Sem permição para buscar a vaga no sistema",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = MensagemErro.class))),
                    @ApiResponse(responseCode = "404", description = "codigo da vaga não cadastrodo no sistema",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = MensagemErro.class)))
            }
    )
    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDTO> getByCodigo(@PathVariable String codigo) {
        Vaga vaga = vagaService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(VagaMapper.toDto(vaga));
    }

}
