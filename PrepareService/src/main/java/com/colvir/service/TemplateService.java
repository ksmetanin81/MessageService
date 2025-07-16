package com.colvir.service;

import com.colvir.dto.MessageDto;
import com.colvir.dto.TemplateDto;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TemplateService {

    MessageDto prepare(MessageDto messageDto) throws TransformerException, ParserConfigurationException, IOException, SAXException;

    List<TemplateDto> getTemplates();

    Optional<TemplateDto> getTemplateById(Long id);

    void save(TemplateDto templateDto);

    boolean delete(Long id);
}
