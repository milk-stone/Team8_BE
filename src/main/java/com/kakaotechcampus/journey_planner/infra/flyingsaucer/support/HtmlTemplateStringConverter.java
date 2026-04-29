package com.kakaotechcampus.journey_planner.infra.flyingsaucer.support;

import com.kakaotechcampus.journey_planner.infra.flyingsaucer.properties.TemplateProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class HtmlTemplateStringConverter {
    private final TemplateEngine templateEngine;
    private final Context context;

    public String convert(TemplateProperties template) {
        return this.templateEngine.process(template.getHtmlPath(), context);
    }
}
