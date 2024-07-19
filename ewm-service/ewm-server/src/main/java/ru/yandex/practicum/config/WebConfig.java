package ru.yandex.practicum.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.yandex.practicum.controller.deserialization.StringToLocalDateTimeConverter;
import ru.yandex.practicum.stats.StatisticsInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final StatisticsInterceptor statisticsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(statisticsInterceptor);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToLocalDateTimeConverter());
    }
}
