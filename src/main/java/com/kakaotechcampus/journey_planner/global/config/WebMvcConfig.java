package com.kakaotechcampus.journey_planner.global.config;

import com.kakaotechcampus.journey_planner.global.resolver.LoginMemberArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://journey-planner-deployment.vercel.app",
                        "http://localhost:5173",
                        "http://121.182.116.155:5137",
                        "https://journey-planner.org",
                        "https://www.journey-planner.org"
                )
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
