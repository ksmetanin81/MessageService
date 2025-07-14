package com.colvir.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class HeaderDto {

    @NotEmpty
    private String messageCode;
    @NotEmpty
    private String processingCode;
    private String parameters;
    private String externalId;
}
