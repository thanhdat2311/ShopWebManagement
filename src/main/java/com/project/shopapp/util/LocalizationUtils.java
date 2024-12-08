package com.project.shopapp.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import java.util.Locale;

@RequiredArgsConstructor
@Component
public class LocalizationUtils {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public String getLocalizationMessages(String messageKey, Object...params){
        HttpServletRequest httpServletRequest = WebUtils.getCurrentRequest();
        Locale locale = localeResolver.resolveLocale(httpServletRequest);
        return messageSource.getMessage(messageKey,params,locale);
    }
}
