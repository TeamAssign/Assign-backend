package com.team3.assign_back.global.config;

import com.team3.assign_back.global.enums.FoodEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        WebMvcConfigurer.super.addFormatters(registry);
        registry.addConverter(new StringToFoodCategoryConverter());
        registry.addConverter(new StringToFoodTypeConverter());
    }

    public static class StringToFoodCategoryConverter implements Converter<String, FoodEnum.FoodCategory> {
        @Override
        public FoodEnum.FoodCategory convert(String source) {
            return FoodEnum.FoodCategory.from(source);
        }

    }

    public static class StringToFoodTypeConverter implements Converter<String, FoodEnum.FoodType> {
        @Override
        public FoodEnum.FoodType convert(String source) {
            return FoodEnum.FoodType.from(source);
        }

    }

}
