package com.example.demoudemyapi.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioCreateDTO {

    @NotBlank
    @Email(message = "Email inválido", regexp = "^[A-Za-z0-9+_.-]+@[a-z0-9]+\\.[a-z]{2,3}$")
    private String username;



    @NotBlank
    @Size(min = 6, max = 6, message = "A senha deve ter 6 caracteres")
    private String password;
}
