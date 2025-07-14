package com.colvir.service.impl;

import com.colvir.dto.MessageDto;
import com.colvir.dto.HeaderDto;
import com.colvir.feign.PrepareServiceClient;
import com.colvir.feign.TransportServiceClient;
import com.colvir.mapper.MessageMapper;
import com.colvir.model.Message;
import com.colvir.repository.MessageRepository;
import com.colvir.service.MessageProducer;
import com.colvir.service.MessagingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.colvir.config.JmsConfig.QUEUE_NEW;

@Service
@RequiredArgsConstructor
public class MessagingServiceImpl implements MessagingService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final MessageProducer messageProducer;
    private final PrepareServiceClient prepareServiceClient;
    private final TransportServiceClient transportServiceClient;

    @Override
    @Transactional
    public void send(HeaderDto headerDto) throws JsonProcessingException {
        Message message = messageMapper.headerToEntity(headerDto);
        messageRepository.save(message);
        messageProducer.send(QUEUE_NEW, messageMapper.toDto(message));
    }

    @Override
    @Transactional
    public MessageDto sendSync(HeaderDto headerDto) {
        Message message = messageMapper.headerToEntity(headerDto);
        messageRepository.save(message);
        MessageDto messageDto = transportServiceClient.send(prepareServiceClient.prepare(messageMapper.toDto(message)));
        messageRepository.save(messageMapper.toEntity(messageDto));
        return messageDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDto> getMessages() {
        return messageRepository.findAll().stream().map(messageMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MessageDto> getMessageById(Long id) {
        return messageRepository.findById(id).map(messageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDto> getMessagesByExternalId(String extId) {
        return messageRepository.findByExternalId(extId).stream().map(messageMapper::toDto).toList();
    }
}
