package com.colvir.service.impl;

import com.colvir.dto.MessageDto;
import com.colvir.dto.TemplateDto;
import com.colvir.mapper.TemplateMapper;
import com.colvir.model.Template;
import com.colvir.repository.CbsRepository;
import com.colvir.repository.TemplateRepository;
import com.colvir.service.TemplateService;
import com.colvir.service.TemplateTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final CbsRepository cbsRepository;
    private final TemplateMapper templateMapper;

    @Override
    @Transactional(readOnly = true)
    public MessageDto prepare(MessageDto messageDto) throws ParserConfigurationException, IOException, TransformerException, SAXException {
        List<Template> templates = templateRepository.findByMessageCodeAndProcessingCode(messageDto.getMessageCode(), messageDto.getProcessingCode());
        if (templates.isEmpty()) {
            throw new NoSuchElementException("Template for messageCode = %s and processingCode = %s not found".formatted(messageDto.getMessageCode(), messageDto.getProcessingCode()));
        }
        if (templates.size() > 1) {
            throw new NoSuchElementException("Too mach templates for messageCode = %s and processingCode = %s".formatted(messageDto.getMessageCode(), messageDto.getProcessingCode()));
        }
        TemplateTransformer templateTransformer = new TemplateTransformer(templates.get(0));
        String cbsData = cbsRepository.getCbsData(templateTransformer.prepareQuery(messageDto.getParameters()));
        log.info("CBS data: {}", cbsData);

        messageDto.setBody(templateTransformer.transform(messageDto, cbsData));
        return messageDto;
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
