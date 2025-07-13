package com.colvir.controller;

import com.colvir.dto.MessageDto;
import com.colvir.dto.TemplateDto;
import com.colvir.service.TemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/templates")
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping("/prepare")
    public MessageDto prepare(@RequestBody @Valid MessageDto messageDto) {
        try {
            return templateService.prepare(messageDto);
        } catch (Exception e) {
            e.printStackTrace();
            messageDto.setError(e.toString());
            return messageDto;
        }
    }

    @GetMapping
    public List<TemplateDto> getTemplates() {
        return templateService.getTemplates();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateDto> getTemplateById(@PathVariable Long id) {
        return templateService.getTemplateById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid TemplateDto templateDto) {
        if (templateDto.getId() != null) {
            throw new IllegalArgumentException("Template id should be null");
        }
        templateService.save(templateDto);
    }

    @PutMapping
    public void update(@RequestBody @Valid TemplateDto templateDto) {
        templateService.getTemplateById(templateDto.getId()).ifPresentOrElse(it -> templateService.save(templateDto), () -> {
            throw new NoSuchElementException("Template not found");
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return templateService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
