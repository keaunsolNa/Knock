package org.zerock.knock.component.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Base64;

/**
 * @author nks
 * @apiNote Image src 주소를 받아 Base64로 인코딩 하거나, Base64 문자열을 받아 image 반환.
 */
@Component
public class ConvertImage {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 이미지를 Base64로 변환하는 유틸리티 메서드.
     * @param imageUrl 이미지의 URL
     * @return Base64로 인코딩된 문자열
     */
    public String convertImageToBase64(String imageUrl) {

        return Base64.getEncoder().encodeToString(imageUrl.getBytes());
    }

    /**
     * 이미지를 Base64로 변환하는 유틸리티 메서드.
     * @param base64String 이미지의 URL
     * @return Base64로 인코딩된 문자열
     */
    public String convertBase64ToUrl(String base64String) {

        return new String(Base64.getDecoder().decode(base64String));

    }
}
