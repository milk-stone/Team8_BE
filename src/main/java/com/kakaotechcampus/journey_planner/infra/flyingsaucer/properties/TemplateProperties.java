package com.kakaotechcampus.journey_planner.infra.flyingsaucer.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * For Thymeleaf Template ENUM
 */

@Getter
@RequiredArgsConstructor
public enum TemplateProperties {
    DEFAULT_TEMPLATE("PdfTemplate.html");

    private final String htmlPath;
}
