package com.kakaotechcampus.journey_planner.infra.config;

import com.kakaotechcampus.journey_planner.infra.flyingsaucer.support.HtmlTemplateStringConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Locale;

@Configuration
public class ThymeleafConfig {
    private static final String TEMPLATE_ENCODING = "UTF-8";
    private static final String TEMPLATE_PREFIX = "WEB-INF/templates/";
    private static final String TEMPLATE_SUFFIX = ".html";

    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(1);
        templateResolver.setPrefix(TEMPLATE_PREFIX);
        templateResolver.setSuffix(TEMPLATE_SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);
        templateResolver.setCharacterEncoding(TEMPLATE_ENCODING);

        return templateResolver;
    }

    @Bean("custom-template-engine")
    public TemplateEngine templateEngine(ClassLoaderTemplateResolver templateResolver) {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    @Bean
    public Context templateContext() {
        Context ctx = new Context(Locale.KOREA);
        ctx.setVariable("context", "this is key");
        return ctx;
    }

    @Bean
    public HtmlTemplateStringConverter htmlTemplateStringConverter(
            TemplateEngine templateEngine,
            Context context
    ) {
        return new HtmlTemplateStringConverter(templateEngine, context);
    }
}
