package com.colvir.service;

import com.colvir.dto.ChannelDto;
import com.colvir.dto.MessageDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ChannelService {

    MessageDto send(MessageDto messageDto) throws IOException, InterruptedException;

    List<ChannelDto> getChannels();

    Optional<ChannelDto> getChannelById(Long id);

    void save(ChannelDto channelDto);

    boolean delete(Long id);
}
