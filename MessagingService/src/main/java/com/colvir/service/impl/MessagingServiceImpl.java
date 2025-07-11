package com.colvir.service.impl;

import com.colvir.dto.MessageDto;
import com.colvir.dto.HeaderDto;
import com.colvir.mapper.MessageMapper;
import com.colvir.model.Message;
import com.colvir.repository.MessageRepository;
import com.colvir.service.MessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessagingServiceImpl implements MessagingService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public void send(HeaderDto headerDto) {
        // фиксируем в журнале
        messageRepository.save(messageMapper.headerToEntity(headerDto));
        // передаем в очередь новых
    }

    @Override
    @Transactional
    public MessageDto sendSync(HeaderDto headerDto) {
        Message message = messageMapper.headerToEntity(headerDto);
        // фиксируем в журнале
        messageRepository.save(message);
        // вызываем сервис подготовки
        // фиксируем тело в журнале
        messageRepository.save(message);
        // вызываем транспортный сервис

        // фиксируем ответ
        messageRepository.save(message);
        return messageMapper.toDto(message);
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

    // TODO Дополнительные выборки по разным условиям (датам, типу сообщения, ПЦ)
    // TODO Реализовать пейджинг


}
