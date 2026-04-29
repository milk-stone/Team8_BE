package com.kakaotechcampus.journey_planner.infra.flyingsaucer;

import com.kakaotechcampus.journey_planner.domain.pdf.PdfGenerator;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.infra.flyingsaucer.properties.FontProperties;
import com.kakaotechcampus.journey_planner.infra.flyingsaucer.support.HtmlTemplateStringConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;
import java.io.OutputStream;

import static com.kakaotechcampus.journey_planner.global.exception.ErrorCode.*;
import static com.kakaotechcampus.journey_planner.infra.flyingsaucer.properties.TemplateProperties.*;

@Component
@RequiredArgsConstructor
public class ITextPdfGenerator implements PdfGenerator {
    private final HtmlTemplateStringConverter htmlConverter;
    private final ITextRenderer renderer;

    @Override
    public void createPdf(OutputStream outputStream) {
        try {
            renderer.setDocumentFromString(htmlConverter.convert(DEFAULT_TEMPLATE));
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new BusinessException(CANNOT_CREATE);
        }
    }

    @Override
    public void addFont(FontProperties font) {
        try {
            renderer
                    .getFontResolver()
                    .addFont(
                            font.getFontResource(),
                            font.getFontPath(),
                            font.getEmbedded()
                    );
        } catch (IOException e) {
            throw new BusinessException(FONT_NOT_FOUND);
        }
    }
}
