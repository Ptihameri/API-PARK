package com.example.demoudemyapi.service;

import com.example.demoudemyapi.entity.Vaga;
import com.example.demoudemyapi.exception.CodigoUniqueViolationException;
import com.example.demoudemyapi.exception.NaoEmcontradoException;
import com.example.demoudemyapi.repository.VagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demoudemyapi.entity.Vaga.StatusVaga.LIVRE;


@RequiredArgsConstructor
@Service
public class VagaService {
    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga salvar(Vaga vaga) {
        try {

            return vagaRepository.save(vaga);
        } catch (DataIntegrityViolationException ex) {
            throw new CodigoUniqueViolationException(String.format("Código '%s' já cadastrado", vaga.getCodigo()));
        }
    }

    @Transactional(readOnly = true)
    public Vaga buscarPorCodigo(String codigo) {
        return vagaRepository.findByCodigo(codigo).orElseThrow(
                () -> new NaoEmcontradoException(String.format("Vaga '%s' não encontrada", codigo))
        );
    }

    @Transactional(readOnly = true)
    public Vaga buscarVagaLivre() {
        return vagaRepository.findFirstByStatus(LIVRE).orElseThrow(
                () -> new NaoEmcontradoException("Não há vagas livres")
        );
    }
}
