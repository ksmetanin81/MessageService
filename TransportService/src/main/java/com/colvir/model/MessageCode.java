package com.colvir.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "message_codes")
public class MessageCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String messageCode;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;
}
