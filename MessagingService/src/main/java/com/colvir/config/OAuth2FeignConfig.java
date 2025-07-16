package com.colvir.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class OAuth2FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new ProxyRequestHeaderInterceptor(AUTHORIZATION);
    }
}
