package com.colvir.service;

import com.colvir.dto.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.colvir.config.JmsConfig.QUEUE_ANSWER;
import static com.colvir.config.JmsConfig.QUEUE_ERROR;
import static com.colvir.config.JmsConfig.QUEUE_PREPARE;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {

    private final ChannelService channelService;
    private final ObjectMapper objectMapper;
    private final MessageProducer messageProducer;

    @JmsListener(destination = QUEUE_PREPARE)
    public void onMessage(TextMessage message) throws JMSException, JsonProcessingException {
        MessageDto messageDto = objectMapper.readValue(message.getText(), MessageDto.class);
        log.info("Received message from {}: {}", QUEUE_PREPARE, messageDto);

        try {
            messageProducer.send(QUEUE_ANSWER, channelService.send(messageDto));
        } catch (Exception e) {
            e.printStackTrace();
            messageDto.setError(e.toString());
            messageProducer.send(QUEUE_ERROR, messageDto);
        }
    }
}