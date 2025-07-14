package com.colvir.dto;

import lombok.Data;

@Data
public class ChannelDto {

    private Long id;

    private String processingCode;
    private String host;
    private String login;
    private String pass;

    // private List<String> messageCodes;
}
