package com.colvir.controller;

import com.colvir.dto.ChannelDto;
import com.colvir.dto.MessageDto;
import com.colvir.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/send")
    public MessageDto send(@RequestBody @Valid MessageDto messageDto) throws IOException, InterruptedException {
        return channelService.send(messageDto);
    }

    @GetMapping
    public List<ChannelDto> getChannels() {
        return channelService.getChannels();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChannelDto> getChannelById(@PathVariable Long id) {
        return channelService.getChannelById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid ChannelDto channelDto) {
        if (channelDto.getId() != null) {
            throw new IllegalArgumentException("Channel id should be null");
        }
        channelService.save(channelDto);
    }

    @PutMapping
    public void update(@RequestBody @Valid ChannelDto channelDto) {
        channelService.getChannelById(channelDto.getId()).ifPresentOrElse(it -> channelService.save(channelDto), () -> {
            throw new NoSuchElementException("Channel not found");
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return channelService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
