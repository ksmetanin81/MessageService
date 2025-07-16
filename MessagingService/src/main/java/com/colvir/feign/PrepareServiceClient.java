package com.colvir.feign;

import com.colvir.config.OAuth2FeignConfig;
import com.colvir.dto.MessageDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PrepareService", configuration = OAuth2FeignConfig.class)
public interface PrepareServiceClient {

    @PostMapping("/prepare")
    MessageDto prepare(@RequestBody @Valid MessageDto messageDto);
}
