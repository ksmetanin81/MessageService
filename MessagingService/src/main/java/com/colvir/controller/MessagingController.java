package com.colvir.controller;

import com.colvir.dto.AnswerDto;
import com.colvir.dto.HeaderDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessagingController {

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    public void send(@RequestBody @Valid HeaderDto headerDto) {

    }

    @PostMapping("/sendSync")
    @ResponseStatus(HttpStatus.CREATED)
    public AnswerDto sendSync(@RequestBody @Valid HeaderDto headerDto) {
        return null;
    }


}
