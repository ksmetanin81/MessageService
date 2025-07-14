package com.colvir.service;

import com.colvir.dto.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageProducer {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public void send(String topic, MessageDto messageDto) throws JsonProcessingException {
        jmsTemplate.convertAndSend(new ActiveMQQueue(topic), objectMapper.writeValueAsString(messageDto), m ->
                {
                    log.info("Sent message to {}: {}", topic, messageDto);
                    return m;
                }
        );
    }
}
