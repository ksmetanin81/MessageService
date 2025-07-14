package com.colvir.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
