package com.example.demoudemyapi.web.controller;

import com.example.demoudemyapi.entity.Cliente;
import com.example.demoudemyapi.jwt.JwtDetalhesDoUsuario;
import com.example.demoudemyapi.repository.projection.ClienteProjection;
import com.example.demoudemyapi.service.ClienteService;
import com.example.demoudemyapi.service.UsuarioService;
import com.example.demoudemyapi.web.dto.ClienteCreateDTO;
import com.example.demoudemyapi.web.dto.ClienteResponseDTO;
import com.example.demoudemyapi.web.dto.PageableDTO;
import com.example.demoudemyapi.web.dto.mapper.ClienteMapper;
import com.example.demoudemyapi.web.dto.mapper.PageableMapper;
import com.example.demoudemyapi.web.exception.MensagemErro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "Clientes", description = "Contem as operaões para os recursos disponiveis para um cliente")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clientes")
@CrossOrigin
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @Operation(summary = "Cria um novo cliente", description = "Recurso para criar um novo cliente vinculado a um usuario" + "O usuario deve estar autenticado por um bearer token de um role cliente",
            security = @SecurityRequirement(name = "Authorization"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                            content = @Content(mediaType = "application/json;charser=UTF-8", schema = @Schema(implementation = ClienteResponseDTO.class))),
                    @ApiResponse(responseCode = "409", description = "Cliente ja cadastrado no sistema",
                            content = @Content(mediaType = "application/json;charser=UTF-8", schema = @Schema(implementation = MensagemErro.class))),
                    @ApiResponse(responseCode = "422", description = "Dados invalidos ou incompletos",
                            content = @Content(mediaType = "application/json;charser=UTF-8", schema = @Schema(implementation = MensagemErro.class))),
                    @ApiResponse(responseCode = "403", description = "Usuario autenticado não autorizado",
                            content = @Content(mediaType = "application/json;charser=UTF-8", schema = @Schema(implementation = MensagemErro.class))),
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDTO> create(@RequestBody @Valid ClienteCreateDTO dto, @AuthenticationPrincipal JwtDetalhesDoUsuario detalhesUsuario) {
        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.buscarId(detalhesUsuario.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toDto(cliente));
    }

    @Operation(summary = "Buscar um cliente", description = "Recurso para Buscar um  cliente pelo ID" + "O usuario deve estar autenticado por um bearer token de um role ADMIN",
            security = @SecurityRequirement(name = "Authorization"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente encontrado com sucesso",
                            content = @Content(mediaType = "application/json;charser=UTF-8", schema = @Schema(implementation = ClienteResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Id não cadastrado no sistema",
                            content = @Content(mediaType = "application/json;charser=UTF-8", schema = @Schema(implementation = MensagemErro.class))),
                    @ApiResponse(responseCode = "403", description = "Usuario autenticado não autorizado, deve ser ADMIN",
                            content = @Content(mediaType = "application/json;charser=UTF-8", schema = @Schema(implementation = MensagemErro.class))),
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> getById(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }

    @Operation(summary = "Buscar todos clientes", description = "Recurso para Buscar todos clientes" + "O usuario deve estar autenticado por um bearer token de um role ADMIN",
            security = @SecurityRequirement(name = "Authorization"),
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representa a pagina retornada"),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                            description = "Representa o toal de elementos por pagina"),
                    @Parameter(in = QUERY, name = "sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id,asc")),
                            description = "Representa a ordenação dos elementos retornados, Aceita multiplos criterios de ordenação")
            },
            responses= {
            @ApiResponse(responseCode = "201", description = "Retorna uma pagina de com todos clientes",
                    content = @Content(mediaType = "application/json;charser=UTF-8", schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Usuario autenticado não autorizado, deve ser ADMIN",
                    content = @Content(mediaType = "application/json;charser=UTF-8", schema = @Schema(implementation = MensagemErro.class))),
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDTO> buscarTodos(@Parameter(hidden = true)@PageableDefault(size = 2, sort = {"nome"}) Pageable pageable) {
        Page<ClienteProjection> clientes = clienteService.buscarTodos(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(clientes));
    }
    @Operation(summary = "Buscar todos clientes", description = "Recurso para buscar detalhes do cliente" + "O usuario deve estar autenticado por um bearer token de um role CLiente",
            security = @SecurityRequirement(name = "Authorization"),
            responses= {
                    @ApiResponse(responseCode = "200", description = "Retorno dos detalhes do cliente",
                            content = @Content(mediaType = "application/json;charser=UTF-8", schema = @Schema(implementation = ClienteResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Usuario autenticado não autorizado, deve ser CLiente",
                            content = @Content(mediaType = "application/json;charser=UTF-8", schema = @Schema(implementation = MensagemErro.class))),
            })
    @GetMapping("/detalhes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDTO> getDetalhes(@AuthenticationPrincipal JwtDetalhesDoUsuario detalhesUsuario) {
        Cliente cliente = clienteService.buscarPorUsuarioID(detalhesUsuario.getId());
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }
}
