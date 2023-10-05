package com.example.demoudemyapi.service;

import com.example.demoudemyapi.entity.Usuario;
import com.example.demoudemyapi.exception.NaoEmcontradoException;
import com.example.demoudemyapi.exception.PasswordInvalidException;
import com.example.demoudemyapi.exception.UsernameEmailException;
import com.example.demoudemyapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    //TODO Adicionar validação de email

    @Transactional
    public Usuario salvar(Usuario usuario) {
        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new UsernameEmailException(String.format("Email %s já cadastrado", usuario.getUsername()));
        }

    }

    //TODO: Verificar bloqueio de usuario
    @Transactional(readOnly = true)
    public Usuario buscarId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new NaoEmcontradoException("Usuario não encontrado")
        );
    }

    @Transactional
    public Usuario editarSenha(Long id, String password, String novaSenha, String confirmacaoSenha) {
        if (!novaSenha.equals(confirmacaoSenha)) {
            throw new PasswordInvalidException("Nova senha e confirmação de senha não conferem");
        }
        Usuario user = buscarId(id);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordInvalidException("Senha não Confere");
        }
        if (user.getPassword().equals(novaSenha)) {
            throw new PasswordInvalidException("Nova senha não pode ser igual a senha atual");
        }
        user.setPassword(passwordEncoder.encode(novaSenha));

        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(
                () -> new NaoEmcontradoException(String.format("Usuario '%s' não encontrado", username))
        );
    }
    @Transactional(readOnly = true)
    public Usuario.Role buscarRolePeloUsuario(String username) {
        return usuarioRepository.findRoleByUsername(username);
    }
}

