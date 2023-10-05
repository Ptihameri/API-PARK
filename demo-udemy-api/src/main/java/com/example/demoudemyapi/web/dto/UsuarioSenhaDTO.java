package com.example.demoudemyapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioSenhaDTO {

    @NotBlank
    @Size(min = 6, max = 6, message = "A senha deve ter 6 caracteres")
    private String senhaAtual;
    @NotBlank
    @Size(min = 6, max = 6, message = "A senha deve ter 6 caracteres")
    private String novaSenha;
    @NotBlank
    @Size(min = 6, max = 6, message = "A senha deve ter 6 caracteres")
    private String confirmacaoSenha;

}
