package com.colvir.feign;

import com.colvir.config.OAuth2FeignConfig;
import com.colvir.dto.MessageDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "TransportService", configuration = OAuth2FeignConfig.class)
public interface TransportServiceClient {

    @PostMapping("/send")
    MessageDto send(@RequestBody @Valid MessageDto messageDto);
}
