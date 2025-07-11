package com.colvir.service.impl;

import com.colvir.dto.MessageDto;
import com.colvir.dto.TemplateDto;
import com.colvir.mapper.TemplateMapper;
import com.colvir.repository.TemplateRepository;
import com.colvir.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final TemplateMapper templateMapper;

    @Override
    public MessageDto prepare(MessageDto messageDto) {
        // запрос к БД АБС
        // формирование контекста
        // xslt преобразование по шаблону
        // возврат тела сообщения
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TemplateDto> getTemplates() {
        return templateRepository.findAll().stream().map(templateMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TemplateDto> getTemplateById(Long id) {
        return templateRepository.findById(id).map(templateMapper::toDto);
    }

    @Override
    @Transactional
    public void save(TemplateDto templateDto) {
        templateRepository.save(templateMapper.toEntity(templateDto));
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (templateRepository.findById(id).isPresent()) {
            templateRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
