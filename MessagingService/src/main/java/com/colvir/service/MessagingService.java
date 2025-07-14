package com.colvir.service;

import com.colvir.dto.MessageDto;
import com.colvir.dto.HeaderDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Optional;

public interface MessagingService {

    void send(HeaderDto headerDto) throws JsonProcessingException;

    MessageDto sendSync(HeaderDto headerDto);

    List<MessageDto> getMessages();

    Optional<MessageDto> getMessageById(Long id);

    List<MessageDto> getMessagesByExternalId(String extId);

}
