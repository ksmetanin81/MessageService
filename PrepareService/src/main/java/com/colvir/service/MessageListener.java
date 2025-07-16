package com.colvir.service;

import com.colvir.dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.colvir.config.JmsConfig.QUEUE_ERROR;
import static com.colvir.config.JmsConfig.QUEUE_NEW;
import static com.colvir.config.JmsConfig.QUEUE_PREPARE;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {

    private final TemplateService templateService;
    private final ObjectMapper objectMapper;
    private final MessageProducer messageProducer;

    @JmsListener(destination = QUEUE_NEW)
    public void onMessage(TextMessage message) throws JMSException, IOException {
        MessageDto messageDto = objectMapper.readValue(message.getText(), MessageDto.class);
        log.info("Received message from {}: {}", QUEUE_NEW, messageDto);

        try {
            messageProducer.send(QUEUE_PREPARE, templateService.prepare(messageDto));
        } catch (Exception e) {
            log.error("Error prepare by the template service", e);
            messageDto.setError(e.toString());
            messageProducer.send(QUEUE_ERROR, messageDto);
        }
    }
}
