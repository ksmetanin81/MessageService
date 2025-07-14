package com.colvir.service.impl;

import com.colvir.dto.MessageDto;
import com.colvir.dto.TemplateDto;
import com.colvir.mapper.TemplateMapper;
import com.colvir.model.Template;
import com.colvir.repository.CbsRepository;
import com.colvir.repository.TemplateRepository;
import com.colvir.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final CbsRepository cbsRepository;
    private final TemplateMapper templateMapper;

    private Map<String, String> toMap(String params) {
        return Arrays.stream(params.split(","))
                .map(pair -> pair.split("=>"))
                .filter(pair -> pair.length == 2)
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));
    }

    private String prepareQuery(String query, Map<String, String> params) {
        String prepareQuery = query;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            prepareQuery = prepareQuery.replaceAll(":" + entry.getKey() + "\\s", entry.getValue() + " ");
        }
        prepareQuery = prepareQuery.replaceAll(":.+?\\s", "'' ");

        System.out.println(prepareQuery);
        return prepareQuery;
    }

    private Document createContext(MessageDto messageDto, String cbsData) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element msgContext = document.createElement("MsgContext");
        document.appendChild(msgContext);
        Element msgInfo = document.createElement("MsgInfo");
        msgContext.appendChild(msgInfo);

        Element msgId = document.createElement("MsgId");
        msgInfo.appendChild(msgId);
        msgId.appendChild(document.createTextNode(messageDto.getId().toString()));

        Element sendDate = document.createElement("SendDate");
        msgInfo.appendChild(sendDate);
        sendDate.appendChild(document.createTextNode(messageDto.getSendDate().toString()));

        Element params = document.createElement("Params");
        msgInfo.appendChild(params);
        for (Map.Entry<String, String> entry : toMap(messageDto.getParameters()).entrySet()) {
            Element param = document.createElement("Param");
            params.appendChild(param);
            param.setAttribute("NAME", entry.getKey());
            param.appendChild(document.createTextNode(entry.getValue()));
        }

        Document data = builder.parse(new InputSource(new StringReader(cbsData)));
        Node node = document.importNode(data.getDocumentElement(), true);
        msgContext.appendChild(node);

        return document;
    }

    private String xsltTranslate(Document context, String template) throws TransformerException {

        Source contextSource = new DOMSource(context);
        Source xsltSource = new StreamSource(new StringReader(template));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer(xsltSource);

        StringWriter writer = new StringWriter();
        transformer.transform(contextSource, new StreamResult(writer));

        System.out.println(writer);
        return writer.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDto prepare(MessageDto messageDto) throws TransformerException, ParserConfigurationException, IOException, SAXException {
        List<Template> templates = templateRepository.findByMessageCodeAndProcessingCode(messageDto.getMessageCode(), messageDto.getProcessingCode());
        if (templates.isEmpty()) {
            throw new NoSuchElementException("Template for messageCode = %s and processingCode = %s not found".formatted(messageDto.getMessageCode(), messageDto.getProcessingCode()));
        }
        if (templates.size() > 1) {
            throw new NoSuchElementException("Too mach templates for messageCode = %s and processingCode = %s".formatted(messageDto.getMessageCode(), messageDto.getProcessingCode()));
        }
        Template template = templates.get(0);
        String cbsData = cbsRepository.getCbsData(prepareQuery(template.getContextQuery(), toMap(messageDto.getParameters())));
        System.out.println(cbsData);

        Document context = createContext(messageDto, cbsData);
        messageDto.setBody(xsltTranslate(context, template.getTemplate()));
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
