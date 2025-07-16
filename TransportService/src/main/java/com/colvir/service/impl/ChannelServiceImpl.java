package com.colvir.service.impl;

import com.colvir.dto.ChannelDto;
import com.colvir.dto.MessageDto;
import com.colvir.model.Channel;
import com.colvir.service.ChannelService;
import com.colvir.repository.ChannelRepository;
import com.colvir.mapper.ChannelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMapper channelMapper;

    @Override
    @Transactional(readOnly = true)
    public MessageDto send(MessageDto messageDto) throws IOException, InterruptedException {
        List<Channel> channels = channelRepository.findByMessageCodeAndProcessingCode(messageDto.getMessageCode(), messageDto.getProcessingCode());
        if (channels.isEmpty()) {
            throw new NoSuchElementException("Channel for messageCode = %s and processingCode = %s not found".formatted(messageDto.getMessageCode(), messageDto.getProcessingCode()));
        }
        if (channels.size() > 1) {
            throw new NoSuchElementException("Too mach channels for messageCode = %s and processingCode = %s".formatted(messageDto.getMessageCode(), messageDto.getProcessingCode()));
        }
        Channel channel = channels.get(0);

        String auth = "%s:%s".formatted(channel.getLogin(), channel.getPass());
        String authBase64 = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(channel.getHost()))
                .header("Authorization", "Basic " + authBase64)
                .POST(HttpRequest.BodyPublishers.ofString(messageDto.getBody()))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        messageDto.setAnswerDate(LocalDateTime.now());
        messageDto.setAnswer(response.body());
        return messageDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelDto> getChannels() {
        return channelRepository.findAll().stream().map(channelMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChannelDto> getChannelById(Long id) {
        return channelRepository.findById(id).map(channelMapper::toDto);
    }

    @Override
    @Transactional
    public void save(ChannelDto channelDto) {
        channelRepository.save(channelMapper.toEntity(channelDto));
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (channelRepository.findById(id).isPresent()) {
            channelRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
