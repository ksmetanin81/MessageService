package com.colvir.feign;

import com.colvir.dto.MessageDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "TransportService")
public interface TransportServiceClient {

    @PostMapping("/api/channels/send")
    MessageDto send(@RequestBody @Valid MessageDto messageDto);
}
