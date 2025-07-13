package com.colvir.controller;

import com.colvir.dto.HeaderDto;
import com.colvir.dto.MessageDto;
import com.colvir.service.MessagingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessagingController {

    private final MessagingService messagingService;

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    public void send(@RequestBody @Valid HeaderDto headerDto) {
        messagingService.send(headerDto);
    }

    @PostMapping("/sendSync")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDto sendSync(@RequestBody @Valid HeaderDto headerDto) {
        return messagingService.sendSync(headerDto);
    }

    @GetMapping
    public List<MessageDto> getMessages() {
        return messagingService.getMessages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDto> getTemplateById(@PathVariable Long id) {
        return messagingService.getMessageById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
