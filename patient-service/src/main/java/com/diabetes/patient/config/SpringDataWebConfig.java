package com.diabetes.patient.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;


/**
 * Active le support web de Spring Data pour la pagination avec sérialisation via DTO,
 * assurant ainsi une structure JSON stable pour les réponses paginées.
 */

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class SpringDataWebConfig {
}
