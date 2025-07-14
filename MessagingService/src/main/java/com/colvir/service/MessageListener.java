package com.colvir.service;

import com.colvir.dto.MessageDto;
import com.colvir.mapper.MessageMapper;
import com.colvir.repository.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.colvir.config.JmsConfig.QUEUE_ANSWER;
import static com.colvir.config.JmsConfig.QUEUE_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {

    private final ObjectMapper objectMapper;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @JmsListener(destination = QUEUE_ANSWER)
    public void onMessage(TextMessage message) throws JMSException, IOException {
        MessageDto messageDto = objectMapper.readValue(message.getText(), MessageDto.class);
        log.info("Received message from {}: {}", QUEUE_ANSWER, messageDto);

        messageRepository.save(messageMapper.toEntity(messageDto));
    }

    @JmsListener(destination = QUEUE_ERROR)
    public void onErrorMessage(TextMessage message) throws JMSException, IOException {
        MessageDto messageDto = objectMapper.readValue(message.getText(), MessageDto.class);
        log.info("Received message from {}: {}", QUEUE_ERROR, messageDto);

        messageRepository.save(messageMapper.toEntity(messageDto));
    }
}
