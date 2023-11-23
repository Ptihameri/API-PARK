package com.example.demoudemyapi.web.dto.mapper;

import com.example.demoudemyapi.entity.ClienteVaga;
import com.example.demoudemyapi.web.dto.EstacionamenteoCreateDTO;
import com.example.demoudemyapi.web.dto.EstacionamenteoResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteVagaMapper {

    public static ClienteVaga toClienteVaga(EstacionamenteoCreateDTO dto){
        return new ModelMapper().map(dto, ClienteVaga.class);
    }
    public static EstacionamenteoResponseDTO toDto(ClienteVaga clienteVaga){
        return new ModelMapper().map(clienteVaga, EstacionamenteoResponseDTO.class);
    }
}
