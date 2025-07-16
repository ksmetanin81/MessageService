package com.colvir.repository;

import com.colvir.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Long> {

    List<Template> findByMessageCodeAndProcessingCode(String messageCode, String processingCode);
}
