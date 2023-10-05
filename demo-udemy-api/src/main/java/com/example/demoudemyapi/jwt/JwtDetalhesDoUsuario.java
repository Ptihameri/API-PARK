package com.example.demoudemyapi.jwt;


import com.example.demoudemyapi.entity.Usuario;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class JwtDetalhesDoUsuario extends User {

    private Usuario usuario;
    public JwtDetalhesDoUsuario(Usuario usuario) {
        super(usuario.getUsername(), usuario.getPassword(), AuthorityUtils.createAuthorityList(usuario.getRole().name()));
        this.usuario = usuario;
    }
    public long getId(){
        return this.usuario.getId();
    }
    public String getRole(){
        return this.usuario.getRole().name();
    }
}