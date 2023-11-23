package com.example.demoudemyapi;

import com.example.demoudemyapi.web.dto.VagaCreateDTO;
import com.example.demoudemyapi.web.exception.MensagemErro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/vagas/vagas-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/vagas/vagas-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VagaIT {
    @Autowired
    WebTestClient testClient;

    @Test
    public void criarVagaValida() {
        testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo@alo.com", "123456"))
                .bodyValue(new VagaCreateDTO("1234", "LIVRE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    public void criarVagainValida() {
        MensagemErro responseBody = testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo@alo.com", "123456"))
                .bodyValue(new VagaCreateDTO("1234332342", "LIVRE"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo@alo.com", "123456"))
                .bodyValue(new VagaCreateDTO("", "LIVRE"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo@alo.com", "123456"))
                .bodyValue(new VagaCreateDTO("1234", "LIVREs"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void criarVagaJaCriada() {
        testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo@alo.com", "123456"))
                .bodyValue(new VagaCreateDTO("AB01", "LIVRE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("status").isEqualTo(409)
                .jsonPath("metodo").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");
    }

    @Test
    public void criarVagaUsuarioInvalido() {
        testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo1@alo.com", "123456"))
                .bodyValue(new VagaCreateDTO("AB00", "LIVRE"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("metodo").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");
    }

    @Test
    public void buscarVagaValida() {
        testClient
                .get()
                .uri("/api/v1/vagas/AB01")
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo(10)
                .jsonPath("codigo").isEqualTo("AB01")
                .jsonPath("status").isEqualTo("LIVRE");

    }

    @Test
    public void buscarVagainValida() {
        testClient
                .get()
                .uri("/api/v1/vagas/AB00")
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("metodo").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vagas/AB00");
    }

    @Test
    public void buscarVagaUsuarioInvalido() {
        testClient
                .get()
                .uri("/api/v1/vagas/AB01")
                .headers(JwtAutentication.getHeaderAtutorization(testClient, "alo1@alo.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("metodo").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vagas/AB01");
    }
}
