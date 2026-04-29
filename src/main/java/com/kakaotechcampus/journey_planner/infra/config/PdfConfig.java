package com.kakaotechcampus.journey_planner.infra.config;

import com.kakaotechcampus.journey_planner.domain.pdf.PdfGenerator;
import com.kakaotechcampus.journey_planner.infra.flyingsaucer.ITextPdfGenerator;
import com.kakaotechcampus.journey_planner.infra.flyingsaucer.support.HtmlTemplateStringConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Configuration
public class PdfConfig {
    @Bean
    @Scope("prototype")
    public ITextRenderer renderer() {
        ITextRenderer renderer = new ITextRenderer();

        SharedContext context = renderer.getSharedContext();
        context.setPrint(true);
        context.setInteractive(false);
        context.getTextRenderer().setSmoothingThreshold(0);

        return renderer;
    }

    @Bean
    public PdfGenerator pdfGenerator(HtmlTemplateStringConverter htmlConverter, ITextRenderer renderer) {
        return new ITextPdfGenerator(htmlConverter, renderer);
    }
}
