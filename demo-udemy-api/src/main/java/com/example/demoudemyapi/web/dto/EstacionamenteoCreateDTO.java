package com.example.demoudemyapi.web.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstacionamenteoCreateDTO {

    @NotBlank
    @Size(min = 7, max = 7)
    @Pattern(regexp = "[A-Z]{3}[0-9][0-9A-Z][0-9]{2}", message = "A placa deve serguir o modelo mercosul 'AAA0A00'")
    private String placa;
    @NotBlank
    private String marca;
    @NotBlank
    private String modelo;
    @NotBlank
    private String cor;
    @NotBlank
    @Size(min = 11, max = 14)
    @CPF
    private String clienteCpf;

}
