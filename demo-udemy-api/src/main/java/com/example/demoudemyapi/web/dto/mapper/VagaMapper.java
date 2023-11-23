package com.example.demoudemyapi.web.dto.mapper;

import com.example.demoudemyapi.entity.Vaga;
import com.example.demoudemyapi.web.dto.VagaCreateDTO;
import com.example.demoudemyapi.web.dto.VagaResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VagaMapper {
    public static Vaga toVaga(VagaCreateDTO dto){
        return new ModelMapper().map(dto, Vaga.class);
    }
    public static VagaResponseDTO toDto(Vaga vaga){
        return new ModelMapper().map(vaga, VagaResponseDTO.class);
    }

}
