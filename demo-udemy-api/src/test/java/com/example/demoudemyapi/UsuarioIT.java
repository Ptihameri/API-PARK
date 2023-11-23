package com.example.demoudemyapi;

import com.example.demoudemyapi.web.dto.UsuarioCreateDTO;
import com.example.demoudemyapi.web.dto.UsuarioResponseDTO;
import com.example.demoudemyapi.web.dto.UsuarioSenhaDTO;
import com.example.demoudemyapi.web.exception.MensagemErro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

    @Autowired
    WebTestClient testeClient;

    //teste para Criar Usuario
    @Test
    public void teste_criarUsuario_validos() {
        UsuarioResponseDTO responseBody = testeClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("alo0@alo.com", "123456"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("alo0@alo.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");

    }

    @Test
    public void teste_criarUsuario_UsernameInvalidos() {
        MensagemErro responseBody = testeClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        responseBody = testeClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("aa", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testeClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("aa@aa", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


    }

    @Test
    public void teste_criarUsuario_SenhaInvalidos() {
        MensagemErro responseBody = testeClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("alo@alo.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        responseBody = testeClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("alo@alo.com", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testeClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("alo@alo.com", "1234562"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


    }

    @Test
    public void teste_criarUsuario_usuarioExistente() {
        MensagemErro responseBody = testeClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

    }

    // teste para Buscar ID
    @Test
    public void teste_buscarId_Valido() {
        UsuarioResponseDTO responseBody = testeClient
                .get()
                .uri("/api/v1/usuarios/130")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(130);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("alo@alo.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");

        responseBody = testeClient
                .get()
                .uri("/api/v1/usuarios/131")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo1@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(131);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("alo1@alo.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");

        responseBody = testeClient
                .get()
                .uri("/api/v1/usuarios/132")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo2@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(132);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("alo2@alo.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");

    }

    @Test
    public void teste_atualizarUsuario_idInvalido() {


        MensagemErro responseBody = testeClient
                .get()
                .uri("/api/v1/usuarios/1000") // ID inválid
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        // Verifica se a resposta contém uma mensagem de erro apropriada
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void teste_atualizarUsuario_erradoInvalido() {


        MensagemErro responseBody = testeClient
                .get()
                .uri("/api/v1/usuarios/132") // ID inválid
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo1@alo.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    // teste para Atualizar Senha
    @Test
    public void teste_atualizarSenha_valido() {

        testeClient
                .patch()
                .uri("/api/v1/usuarios/130")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue((new UsuarioSenhaDTO("123456", "101010", "101010"))).exchange()
                .expectStatus().isOk();

        testeClient
                .patch()
                .uri("/api/v1/usuarios/131")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo1@alo.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue((new UsuarioSenhaDTO("123456", "101010", "101010"))).exchange()
                .expectStatus().isOk();
    }

    @Test
    public void teste_atualizarSenha_Invalida_SenhaNaoConfere() {

        MensagemErro responseBody = testeClient
                .patch()
                .uri("/api/v1/usuarios/130")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue((new UsuarioSenhaDTO("111111", "101010", "101010"))).exchange()
                .expectStatus().isBadRequest().expectBody(MensagemErro.class).returnResult().getResponseBody();
        ;

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
        org.assertj.core.api.Assertions.assertThat(responseBody.getMensagem()).contains("Senha não Confere");

        responseBody = testeClient
                .patch()
                .uri("/api/v1/usuarios/130")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue((new UsuarioSenhaDTO("123456", "101011", "101010"))).exchange()
                .expectStatus().isBadRequest().expectBody(MensagemErro.class).returnResult().getResponseBody();
        ;

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testeClient
                .patch()
                .uri("/api/v1/usuarios/130")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue((new UsuarioSenhaDTO("123456", "101010", "101011"))).exchange()
                .expectStatus().isBadRequest().expectBody(MensagemErro.class).returnResult().getResponseBody();
        ;

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void teste_atualizarSenha_invalidaportamanho() {

        MensagemErro responseBody = testeClient
                .patch()
                .uri("/api/v1/usuarios/131")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue((new UsuarioSenhaDTO("123456", "10101010", "10101010"))).exchange()
                .expectStatus().isEqualTo(422).expectBody(MensagemErro.class).returnResult().getResponseBody();
        ;

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void teste_atualizarSenha_negado() {

        MensagemErro responseBody = testeClient
                .patch()
                .uri("/api/v1/usuarios/135")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue((new UsuarioSenhaDTO("123456", "101010", "101010"))).exchange()
                .expectStatus().isForbidden().expectBody(MensagemErro.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void teste_buscarTodos_valido() {
        List<UsuarioResponseDTO> responseBody = testeClient
                .get()
                .uri("/api/v1/usuarios")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UsuarioResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.size()).isEqualTo(3);
        org.assertj.core.api.Assertions.assertThat(responseBody.get(0).getId()).isEqualTo(130);
        org.assertj.core.api.Assertions.assertThat(responseBody.get(0).getUsername()).isEqualTo("alo@alo.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.get(0).getRole()).isEqualTo("ADMIN");
        org.assertj.core.api.Assertions.assertThat(responseBody.get(1).getId()).isEqualTo(131);
        org.assertj.core.api.Assertions.assertThat(responseBody.get(1).getUsername()).isEqualTo("alo1@alo.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.get(1).getRole()).isEqualTo("CLIENTE");
        org.assertj.core.api.Assertions.assertThat(responseBody.get(2).getId()).isEqualTo(132);
        org.assertj.core.api.Assertions.assertThat(responseBody.get(2).getUsername()).isEqualTo("alo2@alo.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.get(2).getRole()).isEqualTo("CLIENTE");

    }

    @Test
    public void teste_buscarTodos_negado_403() {
        //usuario não adimistrador
        MensagemErro responseBody = testeClient
                .get()
                .uri("/api/v1/usuarios")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo1@alo.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(MensagemErro.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void teste_buscarTodos_semAutentcacao() {
        testeClient
                .get()
                .uri("/api/v1/usuarios")
                .exchange()
                .expectStatus().isUnauthorized();

    }


}
//teste para buscar todos os usuarios




