package com.example.demoudemyapi;

import com.example.demoudemyapi.web.dto.EstacionamenteoCreateDTO;
import com.example.demoudemyapi.web.dto.PageableDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/estacionamentos/estacionamentos-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/estacionamentos/estacionamentos-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class EstacionamentoIT {

    @Autowired
    WebTestClient testeClient;

    @Test
    public void criarEntrada_Valido() {
        EstacionamenteoCreateDTO cerataDTO = EstacionamenteoCreateDTO.builder()
                .placa("AAA0A00").marca("Fiat").modelo("Uno").cor("Branco")
                .clienteCpf("48511218653").build();

        testeClient.post().uri("/api/v1/estacionamentos/entrada")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .bodyValue(cerataDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody().jsonPath("placa").isEqualTo("AAA0A00")
                .jsonPath("marca").isEqualTo("Fiat")
                .jsonPath("modelo").isEqualTo("Uno")
                .jsonPath("cor").isEqualTo("Branco")
                .jsonPath("clienteCpf").isEqualTo("48511218653")
                .jsonPath("vagaCodigo").isNotEmpty()
                .jsonPath("dataEntrada").isNotEmpty()
                .jsonPath("recibo").isNotEmpty();

    }

    @Test
    public void criarEntrada_Invalido403() {
        EstacionamenteoCreateDTO cerataDTO = EstacionamenteoCreateDTO.builder()
                .placa("AAA0A00").marca("Fiat").modelo("Uno").cor("Branco")
                .clienteCpf("48511218653").build();

        testeClient.post().uri("/api/v1/estacionamentos/entrada")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo1@alo.com", "123456"))
                .bodyValue(cerataDTO)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/entrada")
                .jsonPath("metodo").isEqualTo("POST");
    }

    @Test
    public void criarEntrada_Invalido422() {
        EstacionamenteoCreateDTO cerataDTO = EstacionamenteoCreateDTO.builder()
                .placa("AAAAAAA").marca("Fiat").modelo("Uno").cor("Branco")
                .clienteCpf("48511218653").build();

        testeClient.post().uri("/api/v1/estacionamentos/entrada")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .bodyValue(cerataDTO)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody().jsonPath("status").isEqualTo("422")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/entrada")
                .jsonPath("metodo").isEqualTo("POST");

        cerataDTO = EstacionamenteoCreateDTO.builder()
                .placa(" ").marca("Fiat").modelo("Uno").cor("Branco")
                .clienteCpf("48511218653").build();

        testeClient.post().uri("/api/v1/estacionamentos/entrada")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .bodyValue(cerataDTO)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody().jsonPath("status").isEqualTo("422")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/entrada")
                .jsonPath("metodo").isEqualTo("POST");

        cerataDTO = EstacionamenteoCreateDTO.builder()
                .placa("AAA0A00").marca(" ").modelo("Uno").cor("Branco")
                .clienteCpf("48511218653").build();

        testeClient.post().uri("/api/v1/estacionamentos/entrada")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .bodyValue(cerataDTO)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody().jsonPath("status").isEqualTo("422")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/entrada")
                .jsonPath("metodo").isEqualTo("POST");

        cerataDTO = EstacionamenteoCreateDTO.builder()
                .placa("AAA0A00").marca("a").modelo(" ").cor("Branco")
                .clienteCpf("48511218653").build();

        testeClient.post().uri("/api/v1/estacionamentos/entrada")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .bodyValue(cerataDTO)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody().jsonPath("status").isEqualTo("422")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/entrada")
                .jsonPath("metodo").isEqualTo("POST");
        cerataDTO = EstacionamenteoCreateDTO.builder()
                .placa("AAA0A00").marca("a").modelo("a").cor(" ")
                .clienteCpf("48511218653").build();

        testeClient.post().uri("/api/v1/estacionamentos/entrada")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .bodyValue(cerataDTO)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody().jsonPath("status").isEqualTo("422")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/entrada")
                .jsonPath("metodo").isEqualTo("POST");

        cerataDTO = EstacionamenteoCreateDTO.builder()
                .placa("AAA0A00").marca("a").modelo("a").cor("Branco")
                .clienteCpf("12").build();

        testeClient.post().uri("/api/v1/estacionamentos/entrada")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .bodyValue(cerataDTO)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody().jsonPath("status").isEqualTo("422")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/entrada")
                .jsonPath("metodo").isEqualTo("POST");

        cerataDTO = EstacionamenteoCreateDTO.builder()
                .placa("AAA0A00").marca("a").modelo("a").cor("Branco")
                .clienteCpf("4.6..18.180.58-77").build();

        testeClient.post().uri("/api/v1/estacionamentos/entrada")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .bodyValue(cerataDTO)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody().jsonPath("status").isEqualTo("422")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/entrada")
                .jsonPath("metodo").isEqualTo("POST");


    }

    @Test
    public void criarEntrada_Invalido404() {
        EstacionamenteoCreateDTO cerataDTO = EstacionamenteoCreateDTO.builder()
                .placa("AAA0A00").marca("Fiat").modelo("Uno").cor("Branco")
                .clienteCpf("15375748681").build();

        testeClient.post().uri("/api/v1/estacionamentos/entrada")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .bodyValue(cerataDTO)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/entrada")
                .jsonPath("metodo").isEqualTo("POST");
    }

    @Test
    @Sql(scripts = "/sql/estacionamentos-ocupadas/estacionamentos-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/estacionamentos-ocupadas/estacionamentos-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void criarEntrada_Invalido404_vagasOcupadas() {
        EstacionamenteoCreateDTO cerataDTO = EstacionamenteoCreateDTO.builder()
                .placa("AAA0A00").marca("Fiat").modelo("Uno").cor("Branco")
                .clienteCpf("46181805877").build();

        testeClient.post().uri("/api/v1/estacionamentos/entrada")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .bodyValue(cerataDTO)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/entrada")
                .jsonPath("metodo").isEqualTo("POST");
    }

    //buscar recibo
    @Test
    public void buscarRecibo_Valido() {
        testeClient.get().uri("/api/v1/estacionamentos/entrada/{recibo}", "20230303-101010")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("ABCD123")
                .jsonPath("marca").isEqualTo("Mclaren")
                .jsonPath("modelo").isEqualTo("F1")
                .jsonPath("cor").isEqualTo("laranja")
                .jsonPath("clienteCpf").isEqualTo("48511218653")
                .jsonPath("vagaCodigo").isEqualTo("AB01")
                .jsonPath("dataEntrada").isEqualTo("03/03/2023 10:10:10")
                .jsonPath("recibo").isEqualTo("20230303-101010");
    }

    @Test
    public void buscarRecibo_ValidoCliente() {
        testeClient.get().uri("/api/v1/estacionamentos/entrada/{recibo}", "20230303-101010")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo1@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("ABCD123")
                .jsonPath("marca").isEqualTo("Mclaren")
                .jsonPath("modelo").isEqualTo("F1")
                .jsonPath("cor").isEqualTo("laranja")
                .jsonPath("clienteCpf").isEqualTo("48511218653")
                .jsonPath("vagaCodigo").isEqualTo("AB01")
                .jsonPath("dataEntrada").isEqualTo("03/03/2023 10:10:10")
                .jsonPath("recibo").isEqualTo("20230303-101010");
    }

    @Test
    public void buscarRecibo_inValidoCliente() {
        testeClient.get().uri("/api/v1/estacionamentos/entrada/{recibo}", "20230303-101111")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo1@alo.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/entrada/20230303-101111")
                .jsonPath("metodo").isEqualTo("GET");
    }

    @Test
    public void saida_Valido() {
        testeClient.put().uri("/api/v1/estacionamentos/saida/{recibo}", "20230303-101010")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("ABCD123")
                .jsonPath("marca").isEqualTo("Mclaren")
                .jsonPath("modelo").isEqualTo("F1")
                .jsonPath("cor").isEqualTo("laranja")
                .jsonPath("clienteCpf").isEqualTo("48511218653")
                .jsonPath("vagaCodigo").isEqualTo("AB01")
                .jsonPath("dataEntrada").isEqualTo("03/03/2023 10:10:10")
                .jsonPath("recibo").isEqualTo("20230303-101010")
                .jsonPath("dataSaida").exists()
                .jsonPath("desconto").exists()
                .jsonPath("valor").exists();
    }

    @Test
    public void saida_inValido_nãoencontrado() {
        testeClient.put().uri("/api/v1/estacionamentos/saida/{recibo}", "20230303-101111")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/saida/20230303-101111")
                .jsonPath("metodo").isEqualTo("PUT");
    }

    @Test
    public void saida_inValido_cliente() {
        testeClient.put().uri("/api/v1/estacionamentos/saida/{recibo}", "20230303-101111")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo1@alo.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/saida/20230303-101111")
                .jsonPath("metodo").isEqualTo("PUT");
    }

    @Test
    public void buscarTodosPorCpf() {
        PageableDTO dto = testeClient.get().uri("/api/v1/estacionamentos/cpf/{cpf}?size=1&page=0", "46181805877")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(dto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(dto.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(dto.getTotalPages()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(dto.getSize()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(dto.getNumber()).isEqualTo(0);


        dto = testeClient.get().uri("/api/v1/estacionamentos/cpf/{cpf}?size=1&page=1", "46181805877")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(dto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(dto.getContent().size()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(dto.getTotalPages()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(dto.getSize()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(dto.getNumber()).isEqualTo(1);

    }

    @Test
    public void buscarTodosPorCpf_Cliente() {
        testeClient.get().uri("/api/v1/estacionamentos/cpf/{cpf}", "46181805877")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo1@alo.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/cpf/46181805877")
                .jsonPath("metodo").isEqualTo("GET");
    }

    @Test
    public void buscarTodosCliente() {
        PageableDTO dto = testeClient.get().uri("/api/v1/estacionamentos?size=1&page=0")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo1@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(dto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(dto.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(dto.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(dto.getSize()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(dto.getNumber()).isEqualTo(0);


        dto =  testeClient.get().uri("/api/v1/estacionamentos?size=1&page=1")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo1@alo.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(dto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(dto.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(dto.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(dto.getSize()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(dto.getNumber()).isEqualTo(1);

    }

    @Test
    public void buscarTodosClienteAdmin() {
        testeClient.get().uri("/api/v1/estacionamentos")
                .headers(JwtAutentication.getHeaderAtutorization(testeClient, "alo@alo.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos")
                .jsonPath("metodo").isEqualTo("GET");
    }



}
