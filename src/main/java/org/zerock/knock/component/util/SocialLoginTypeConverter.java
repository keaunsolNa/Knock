package org.zerock.knock.component.util;

import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.zerock.knock.dto.Enum.SocialLoginType;

/**
 * @author nks
 * @apiNote Social Login Type(Google, NAVER, KAKAO)를 받아 각각의 enum 으로 전환한다.
 */
@Configuration
public class SocialLoginTypeConverter implements Converter<String, SocialLoginType> {

    /**
     * 다음 페이지가 버튼으로 제어될 경우
     * @param type 매개변수로 받은 Social Login Type
     * @return 대문자로 변환된 Social Login Type
     */
    @Override
    public @NonNull SocialLoginType convert(String type) {
        return SocialLoginType.valueOf(type.toUpperCase());
    }
}