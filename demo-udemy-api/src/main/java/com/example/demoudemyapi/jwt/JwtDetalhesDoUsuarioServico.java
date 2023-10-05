package com.example.demoudemyapi.jwt;

import com.example.demoudemyapi.entity.Usuario;
import com.example.demoudemyapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtDetalhesDoUsuarioServico implements UserDetailsService {

     private final UsuarioService usuarioService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorUsername(username);
        return new JwtDetalhesDoUsuario(usuario);
    }
    public JwtToken getJwtTokenAcesso(String username){
        Usuario.Role role = usuarioService.buscarRolePeloUsuario(username);
        return JwtUtils.createToken(username, role.name().substring("ROLE_".length()));
    }
}