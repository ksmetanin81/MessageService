package com.colvir.service;

import com.colvir.dto.AnswerDto;
import com.colvir.dto.MessageDto;
import com.colvir.dto.HeaderDto;

import java.util.List;
import java.util.Optional;

public interface MessagingService {

    void send(HeaderDto headerDto);

    MessageDto sendSync(HeaderDto headerDto);

    List<MessageDto> getMessages();

    Optional<MessageDto> getMessageById(Long id);

}
