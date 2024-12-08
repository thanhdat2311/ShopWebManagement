package com.project.shopapp.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class LanguageConfig{
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages"); // Đường dẫn đến các tệp ngôn ngữ
        messageSource.setDefaultEncoding("UTF-8"); // Đảm bảo mã hóa UTF-8
        messageSource.setUseCodeAsDefaultMessage(true); // Trả về key nếu không tìm thấy bản dịch
        return messageSource;
    }
}
// 6:21
