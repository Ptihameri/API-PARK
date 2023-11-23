package com.example.demoudemyapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClienteCreateDTO {
    @NotBlank
    @Size(min = 3, max = 100)
    private String nome;
    @Size(min = 11, max = 14)
    @CPF
    private String cpf;
}
