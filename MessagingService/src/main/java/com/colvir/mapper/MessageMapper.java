package com.colvir.mapper;

import com.colvir.dto.MessageDto;
import com.colvir.dto.HeaderDto;
import com.colvir.model.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageMapper {

    public MessageDto toDto(Message message) {
        if (message == null) {
            return null;
        }

        MessageDto messageDto = new MessageDto();

        messageDto.setId(message.getId());
        messageDto.setSendDate(message.getSendDate());
        messageDto.setMessageCode(message.getMessageCode());
        messageDto.setProcessingCode(message.getProcessingCode());
        messageDto.setParameters(message.getParameters());
        messageDto.setExternalId(message.getExternalId());
        messageDto.setBody(message.getBody());
        messageDto.setAnswerDate(message.getAnswerDate());
        messageDto.setAnswer(message.getAnswer());
        messageDto.setError(message.getError());

        return messageDto;
    }

    public Message toEntity(MessageDto messageDto) {
        if (messageDto == null) {
            return null;
        }

        Message message = new Message();

        message.setId(messageDto.getId());
        message.setSendDate(messageDto.getSendDate());
        message.setMessageCode(messageDto.getMessageCode());
        message.setProcessingCode(messageDto.getProcessingCode());
        message.setParameters(messageDto.getParameters());
        message.setExternalId(messageDto.getExternalId());
        message.setBody(messageDto.getBody());
        message.setAnswerDate(messageDto.getAnswerDate());
        message.setAnswer(messageDto.getAnswer());
        message.setError(messageDto.getError());

        return message;
    }

    public Message headerToEntity(HeaderDto headerDto) {
        if (headerDto == null) {
            return null;
        }

        Message message = new Message();

        message.setSendDate(LocalDateTime.now());
        message.setMessageCode(headerDto.getMessageCode());
        message.setProcessingCode(headerDto.getProcessingCode());
        message.setParameters(headerDto.getParameters());
        message.setExternalId(headerDto.getExternalId());

        return message;
    }
}
