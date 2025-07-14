package com.colvir.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JmsConfig {

    public static final String QUEUE_NEW = "message.new";
    public static final String QUEUE_ANSWER = "message.answer";
    public static final String QUEUE_ERROR = "message.error";
}
