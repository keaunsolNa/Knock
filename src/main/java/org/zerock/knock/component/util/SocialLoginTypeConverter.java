package org.zerock.knock.component.util;

import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.zerock.knock.dto.Enum.SocialLoginType;

@Configuration
public class SocialLoginTypeConverter implements Converter<String, SocialLoginType> {
    @Override
    public @NonNull SocialLoginType convert(String s) {
        return SocialLoginType.valueOf(s.toUpperCase());
    }
}