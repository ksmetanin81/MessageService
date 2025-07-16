package com.colvir.service;

import com.colvir.dto.MessageDto;
import com.colvir.model.Template;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class TemplateTransformer {

    private Template template;

    private Map<String, String> toMap(String params) {
        return Arrays.stream(params.split(","))
                .map(pair -> pair.split("=>"))
                .filter(pair -> pair.length == 2)
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));
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

    public String prepareQuery(String params) {
        String prepareQuery = template.getContextQuery();
        for (Map.Entry<String, String> entry : toMap(params).entrySet()) {
            prepareQuery = prepareQuery.replaceAll(":" + entry.getKey() + "\\s", entry.getValue() + " ");
        }
        prepareQuery = prepareQuery.replaceAll(":.+?\\s", "'' ");

        log.info("Prepared query: {}", prepareQuery);
        return prepareQuery;
    }

    public String transform(MessageDto messageDto, String cbsData) throws TransformerException, ParserConfigurationException, IOException, SAXException {
        Document context = createContext(messageDto, cbsData);

        Source contextSource = new DOMSource(context);
        Source xsltSource = new StreamSource(new StringReader(template.getTemplate()));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer(xsltSource);

        StringWriter writer = new StringWriter();
        transformer.transform(contextSource, new StreamResult(writer));

        log.info("Transformed template: {}", writer);
        return writer.toString();
    }
}
