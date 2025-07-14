package com.colvir.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDto {

    private Long id;
    private LocalDateTime sendDate;
    private String messageCode;
    private String processingCode;
    private String parameters;
    private String externalId;
    private String body;
    private LocalDateTime answerDate;
    private String answer;
    private String error;

}
