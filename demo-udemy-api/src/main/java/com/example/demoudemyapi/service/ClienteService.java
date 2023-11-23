package com.example.demoudemyapi.service;

import com.example.demoudemyapi.entity.Cliente;
import com.example.demoudemyapi.exception.CpfJaCadastradorException;
import com.example.demoudemyapi.exception.NaoEmcontradoException;
import com.example.demoudemyapi.repository.ClienteRepository;
import com.example.demoudemyapi.repository.projection.ClienteProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente salvar(Cliente cliente) {
        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException e) {
            throw new CpfJaCadastradorException(
                    String.format("O CPF %s já está cadastrado no sistema", cliente.getCpf()));
        }
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NaoEmcontradoException(String.format("Cliente com id %s não encontrado", id)));
    }

    @Transactional(readOnly = true)
    public Page<ClienteProjection> buscarTodos(Pageable pageable) {
        return clienteRepository.buscaTodos(pageable);
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorUsuarioID(long id) {
        return clienteRepository.findByUsuarioId(id);
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf).orElseThrow(() -> new NaoEmcontradoException(
                String.format("Cliente com CPF '%s' não encontrado", cpf)));
    }
}
