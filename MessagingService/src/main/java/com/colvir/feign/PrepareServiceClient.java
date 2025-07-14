package com.colvir.feign;

import com.colvir.dto.MessageDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PrepareService")
public interface PrepareServiceClient {

    @PostMapping("/api/templates/prepare")
    MessageDto prepare(@RequestBody @Valid MessageDto messageDto);
}
