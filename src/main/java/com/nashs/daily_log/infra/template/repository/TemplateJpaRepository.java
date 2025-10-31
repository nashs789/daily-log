package com.nashs.daily_log.infra.template.repository;

import com.nashs.daily_log.infra.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateJpaRepository extends JpaRepository<Template, Long> {
}
