package com.example.demoudemyapi.service;

import com.example.demoudemyapi.entity.Cliente;
import com.example.demoudemyapi.entity.ClienteVaga;
import com.example.demoudemyapi.entity.Vaga;
import com.example.demoudemyapi.util.EstacionamenteUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.example.demoudemyapi.entity.Vaga.StatusVaga.LIVRE;

@Service
@RequiredArgsConstructor
public class EstacionamentoService {

    public final ClienteVagaService clienteVagaService;
    public final VagaService vagaService;
    public final ClienteService clienteService;

    @Transactional
    public ClienteVaga entrada(ClienteVaga clienteVaga) {
        Cliente cliente = clienteService.buscarPorCpf(clienteVaga.getCliente().getCpf());
        clienteVaga.setCliente(cliente);

        Vaga vaga = vagaService.buscarVagaLivre();
        vaga.setStatus(Vaga.StatusVaga.OCUPADA);
        clienteVaga.setVaga(vaga);

        clienteVaga.setDataEntrada(LocalDateTime.now());
        clienteVaga.setRecibo(EstacionamenteUtil.gerarRecibo());

        return clienteVagaService.salvar(clienteVaga);
    }

    @Transactional
    public ClienteVaga saida(String recibo) {
        ClienteVaga clienteVaga = clienteVagaService.getByRecibo(recibo);
        LocalDateTime dataSaida = LocalDateTime.now();
        BigDecimal valor = EstacionamenteUtil.calcularCusto(clienteVaga.getDataEntrada(), dataSaida);
        clienteVaga.setValor(valor);

        long totalDeVezes = clienteVagaService.getFidelidade(clienteVaga.getCliente().getCpf());

        BigDecimal desconto = EstacionamenteUtil.calcularDesconto(valor, totalDeVezes);
        clienteVaga.setDesconto(desconto);
        clienteVaga.setDataSaida(dataSaida);
        clienteVaga.getVaga().setStatus(LIVRE);
        return clienteVagaService.salvar(clienteVaga);
    }
}
