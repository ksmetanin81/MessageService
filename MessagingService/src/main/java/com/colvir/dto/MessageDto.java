package com.colvir.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MessageDto {

    private Long id;
    private LocalDate sendDate;
    private String messageCode;
    private String processingCode;
    private String parameters;
    private String body;
    private LocalDate answerDate;
    private String answer;
    private String error;

}
