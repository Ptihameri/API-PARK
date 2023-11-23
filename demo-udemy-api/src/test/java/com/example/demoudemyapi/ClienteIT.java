package com.example.demoudemyapi;

import com.example.demoudemyapi.web.dto.ClienteCreateDTO;
import com.example.demoudemyapi.web.dto.ClienteResponseDTO;
import com.example.demoudemyapi.web.dto.PageableDTO;
import com.example.demoudemyapi.web.exception.MensagemErro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clientes/clientes-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clientes/clientes-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClienteIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createCliente_valido() {
        ClienteResponseDTO responseDTO = testClient
                .post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo3@alo.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Tigas", "34628802840"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDTO.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDTO.getNome()).isEqualTo("Tigas");
        org.assertj.core.api.Assertions.assertThat(responseDTO.getCpf()).isEqualTo("34628802840");

    }

    @Test
    public void createCliente_jaCadastarado() {
        MensagemErro responseBody = testClient
                .post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo3@alo.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Tigas", "46181805877"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

        responseBody = testClient
                .post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo2@alo.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Tigas", "06107398910"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

    }

    @Test
    public void createCliente_invalido() {
        MensagemErro responseBody = testClient
                .post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo3@alo.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Tigas", "461818"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo3@alo.com", "123456"))
                .bodyValue(new ClienteCreateDTO("", "46181805877"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        responseBody = testClient
                .post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo3@alo.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Tigas", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo3@alo.com", "123456"))
                .bodyValue(new ClienteCreateDTO("t", "46181805877"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo3@alo.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Tigas", "444444444444444444444444444"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo3@alo.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Tigas", "1234567809"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo3@alo.com", "123456"))
                .bodyValue(new ClienteCreateDTO("TigasTigasTigasTigasTigasTigasTigTigasasTigasTigasTigasTigasTigasTigasTigasTigasTigasTigasTigasTigasTigas", "461818"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public void createCliente_adm() {
        MensagemErro responseBody = testClient
                .post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo@alo.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Tigas", "34628802840"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }
    @Test
    public void buscarCliente_Valido() {
        ClienteResponseDTO responseBody = testClient
                .get()
                .uri("api/v1/clientes/10")
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(10);
    }
    @Test
    public void buscarCliente_loginInValido_403() {
        MensagemErro responseBody = testClient
                .get()
                .uri("api/v1/clientes/10")
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo1@alo.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }
    @Test
    public void buscarCliente_idInValido_404() {
        MensagemErro responseBody = testClient
                .get()
                .uri("api/v1/clientes/11")
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }
    @Test
    public void buscarTodos_ADM_valido() {
        PageableDTO responseBody = testClient
                .get()
                .uri("api/v1/clientes")
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(3);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(1);

        responseBody = testClient
                .get()
                .uri("api/v1/clientes?size=1&page=1")
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(3);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(3);
    }
    @Test
    public void buscarTodos_ADM_invalido() {
        MensagemErro responseBody = testClient
                .get()
                .uri("api/v1/clientes")
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo1@alo.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }
    @Test
    public void buscarDetalhes_clienteValido() {
        ClienteResponseDTO responseBody = testClient
                .get()
                .uri("api/v1/clientes/detalhes")
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo2@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("46181805877");
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Tihas");
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(20);
    }@Test
    public void buscarDetalhes_clienteInvalido() {
        MensagemErro responseBody = testClient
                .get()
                .uri("api/v1/clientes/detalhes")
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }


}
