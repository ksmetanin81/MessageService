package com.colvir.service;

import com.colvir.dto.MessageDto;
import com.colvir.dto.TemplateDto;

import java.util.List;
import java.util.Optional;

public interface TemplateService {

    MessageDto prepare(MessageDto messageDto);

    List<TemplateDto> getTemplates();

    Optional<TemplateDto> getTemplateById(Long id);

    void save(TemplateDto templateDto);

    boolean delete(Long id);
}
