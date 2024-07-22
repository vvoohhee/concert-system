package io.hhplus.concert.interfaces.config;

import io.hhplus.concert.interfaces.presentation.token.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor())
                .addPathPatterns(
                        "api/concert/queue/**",
                        "api/payment/queue/**"
                );
    }
}
