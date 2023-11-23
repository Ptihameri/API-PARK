package com.example.demoudemyapi.web.controller;

import com.example.demoudemyapi.entity.ClienteVaga;
import com.example.demoudemyapi.jwt.JwtDetalhesDoUsuario;
import com.example.demoudemyapi.repository.projection.ClienteVagaProjection;
import com.example.demoudemyapi.service.ClienteService;
import com.example.demoudemyapi.service.ClienteVagaService;
import com.example.demoudemyapi.service.EstacionamentoService;
import com.example.demoudemyapi.service.JasperService;
import com.example.demoudemyapi.web.dto.EstacionamenteoCreateDTO;
import com.example.demoudemyapi.web.dto.EstacionamenteoResponseDTO;
import com.example.demoudemyapi.web.dto.PageableDTO;
import com.example.demoudemyapi.web.dto.mapper.ClienteVagaMapper;
import com.example.demoudemyapi.web.dto.mapper.PageableMapper;
import com.example.demoudemyapi.web.exception.MensagemErro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "Estacionamento", description = "Contem operações de usuarios")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/estacionamentos")
@CrossOrigin
public class EstacionamentoController {

    private final EstacionamentoService estacionamentoService;
    private final ClienteVagaService clienteVagaService;
    private final ClienteService clienteService;
    private final JasperService jasperService;

    @Operation(summary = "Entrada de veiculo", description = "Operação para entrada de veiculo no estacionamento exige perfil ADMIN"
            , security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Entrada realizada com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "Localização do recurso criado"),
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EstacionamenteoResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado no sistema ou vaga não disponivel",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MensagemErro.class))),
                    @ApiResponse(responseCode = "422", description = "Falta de dados ou dados invalidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MensagemErro.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado ao perfil CLIENTE",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MensagemErro.class))),
            })
    @PostMapping("/entrada")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstacionamenteoResponseDTO> entrada(@RequestBody @Valid EstacionamenteoCreateDTO dto) {
        ClienteVaga clienteVaga = ClienteVagaMapper.toClienteVaga(dto);
        estacionamentoService.entrada(clienteVaga);
        EstacionamenteoResponseDTO responseDTO = ClienteVagaMapper.toDto(clienteVaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{recibo}")
                .buildAndExpand(clienteVaga.getRecibo())
                .toUri();
        return ResponseEntity.created(location).body(responseDTO);

    }

    @Operation(summary = "Entrada de veiculo", description = "Operação para entrada de veiculo no estacionamento exige perfil ADMIN"
            , security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recibo encontrado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EstacionamenteoResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "recibo não encontrado no sistema ou ja realizou a saida",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MensagemErro.class))),
            })
    @GetMapping("/entrada/{recibo}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<EstacionamenteoResponseDTO> buscarEntrada(@PathVariable String recibo) {
        ClienteVaga clienteVaga = clienteVagaService.getByRecibo(recibo);
        EstacionamenteoResponseDTO responseDTO = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Buscar Vagas do cliente", description = "Operação para buscar vagas utilizadas no estacionamento por um cliente, exige perfil ADMIN"
            ,  security = @SecurityRequirement(name = "Authorization"),
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representa a pagina retornada"),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5")),
                            description = "Representa o toal de elementos por pagina"),
                    @Parameter(in = QUERY, name = "sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "dataEntrada,asc")),
                            description = "Campo padrão de ordenação 'dataEntrada,asc'")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vaga(s) encontrada(s) com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EstacionamenteoResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado ao perfil CLIENTE",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MensagemErro.class))),
            })
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDTO> buscarEntradas(@PathVariable String cpf, @PageableDefault(size = 5, sort = "dataEntrada", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ClienteVagaProjection> projectionPage = clienteVagaService.getByCpf(cpf, pageable);
        PageableDTO dto = PageableMapper.toDto(projectionPage);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Buscar Vagas do cliente", description = "Operação para buscar vagas utilizadas no estacionamento por um cliente, exige perfil CLIENTE"
            ,  security = @SecurityRequirement(name = "Authorization"),
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representa a pagina retornada"),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5")),
                            description = "Representa o toal de elementos por pagina"),
                    @Parameter(in = QUERY, name = "sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "dataEntrada,asc")),
                            description = "Campo padrão de ordenação 'dataEntrada,asc'")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vaga(s) encontrada(s) com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EstacionamenteoResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado ao perfil ADMIN",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MensagemErro.class))),
            })
    @GetMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PageableDTO> buscarEntradasCliente(@AuthenticationPrincipal JwtDetalhesDoUsuario user,
                                                             @PageableDefault(size = 5, sort = "dataEntrada", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ClienteVagaProjection> projectionPage = clienteVagaService.getByCpfCliente(user.getId(), pageable);
        PageableDTO dto = PageableMapper.toDto(projectionPage);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "saida de veiculo", description = "Operação para saida de veiculo no estacionamento exige perfil ADMIN"
            ,  security = @SecurityRequirement(name = "Authorization"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Saida realizada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EstacionamenteoResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Recibo não encontrado no sistema ou ja realizou a saida",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MensagemErro.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado ao perfil CLIENTE",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MensagemErro.class))),
            })
    @PutMapping("/saida/{recibo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstacionamenteoResponseDTO> saida(@PathVariable String recibo) {
        ClienteVaga clienteVaga = estacionamentoService.saida(recibo);
        EstacionamenteoResponseDTO responseDTO = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/relatorio")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> getRelatorio(HttpServletResponse response, @AuthenticationPrincipal JwtDetalhesDoUsuario user) throws IOException {
        String cpf = clienteService.buscarPorUsuarioID(user.getId()).getCpf();
        jasperService.addParametro("CPF", cpf);

        byte[] bytes = jasperService.gerarPDF();

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-disposition", "inline; filename=relatorio.pdf");
        response.getOutputStream().write(bytes);

        return ResponseEntity.ok().build();

    }

}
