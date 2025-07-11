package com.colvir.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TemplateDto {

    @Min(0)
    private Long id;
    @NotEmpty
    private String messageCode;
    @NotEmpty
    private String processingCode;
    private String contextQuery;
    @NotEmpty
    private String template;
}
