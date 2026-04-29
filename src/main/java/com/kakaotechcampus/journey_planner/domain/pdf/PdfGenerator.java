package com.kakaotechcampus.journey_planner.domain.pdf;

import com.kakaotechcampus.journey_planner.infra.flyingsaucer.properties.FontProperties;

import java.io.OutputStream;

public interface PdfGenerator {
    void createPdf(OutputStream outputStream);

    void addFont(FontProperties font);
}
