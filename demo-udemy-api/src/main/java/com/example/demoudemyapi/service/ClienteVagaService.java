package com.example.demoudemyapi.service;

import com.example.demoudemyapi.entity.ClienteVaga;
import com.example.demoudemyapi.exception.NaoEmcontradoException;
import com.example.demoudemyapi.repository.ClienteVagaRepository;
import com.example.demoudemyapi.repository.projection.ClienteVagaProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClienteVagaService {

    private final ClienteVagaRepository clienteVagaRepository;

    @Transactional
    public ClienteVaga salvar(ClienteVaga clienteVaga){
        return clienteVagaRepository.save(clienteVaga);
    }

    @Transactional(readOnly = true)
    public ClienteVaga getByRecibo(String recibo) {
        return clienteVagaRepository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(
                () -> new NaoEmcontradoException(String.format("Recibo não encontrado ou ja saiu do estacionamento %s", recibo))
        );
    }
    @Transactional(readOnly = true)
    public long getFidelidade(String cpf) {
            return clienteVagaRepository.countByClienteCpfAndDataSaidaIsNotNull(cpf);
    }
    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> getByCpf(String cpf, Pageable pageable) {
        return clienteVagaRepository.findAllByClienteCpf(cpf, pageable);
    }

    public Page<ClienteVagaProjection> getByCpfCliente(long id, Pageable pageable) {
        return clienteVagaRepository.findAllByClienteUsuarioId(id, pageable);
    }
}
