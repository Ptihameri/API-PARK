package com.example.demoudemyapi.repository;

import com.example.demoudemyapi.entity.Cliente;
import com.example.demoudemyapi.repository.projection.ClienteProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query("select c from Cliente c")
    Page<ClienteProjection> buscaTodos(Pageable pageable);

    Cliente findByUsuarioId(long id);

    Optional<Cliente> findByCpf(String cpf);
}
