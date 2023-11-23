package com.example.demoudemyapi.web.dto.mapper;

import com.example.demoudemyapi.web.dto.PageableDTO;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

public class PageableMapper {
    public static PageableDTO toDto(Page page) {
        return new ModelMapper().map(page, PageableDTO.class);
    }
}
