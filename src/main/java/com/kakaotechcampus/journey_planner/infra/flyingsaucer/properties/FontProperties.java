package com.kakaotechcampus.journey_planner.infra.flyingsaucer.properties;

import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.lowagie.text.pdf.BaseFont;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static com.kakaotechcampus.journey_planner.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Getter
public enum FontProperties {
    DEFAULT_FONT("META-INF/font/GowunDodum-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED),
    ;

    private final String fontPath;
    private final String encoding;
    private final Boolean embedded;

    public String getFontResource() {
        try {
            return new ClassPathResource(this.fontPath)
                    .getURL()
                    .toString();
        } catch (IOException e) {
            throw new BusinessException(RESOURCE_NOT_FOUND);
        }
    }
}
