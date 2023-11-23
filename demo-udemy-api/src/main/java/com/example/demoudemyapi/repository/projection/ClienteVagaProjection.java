package com.example.demoudemyapi.repository.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ClienteVagaProjection {

     String getPlaca();
     String getMarca();
     String getModelo();
     String getCor();
     String getClienteCpf();
     String getRecibo();
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
     LocalDateTime getDataEntrada();
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
     LocalDateTime getDataSaida();
     String getVagaCodigo();
    @JsonFormat(pattern = "R$ #,##0.00")
     BigDecimal getValor();
    @JsonFormat(pattern = "R$ #,##0.00")
     BigDecimal getDesconto();
}
