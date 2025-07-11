package com.colvir.mapper;

import com.colvir.dto.TemplateDto;
import com.colvir.model.Template;
import org.mapstruct.Mapper;

@Mapper
public interface TemplateMapper {

    TemplateDto toDto(Template template);

    Template toEntity(TemplateDto goalDto);
}
