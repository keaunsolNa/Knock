package org.knock.knock_back.service.oAuth;

import org.knock.knock_back.dto.Enum.SocialLoginType;

public interface SocialOauth {
    String getOauthRedirectURL();
    String requestAccessToken(String code);
    String requestUserInfo(String accessToken);

    default SocialLoginType type() {
        return switch (this) {
            case GoogleOauth googleOauth -> SocialLoginType.GOOGLE;
            case NaverOauth naverOauth -> SocialLoginType.NAVER;
            case KakaoOauth kakaoOauth -> SocialLoginType.KAKAO;
            default -> null;
        };
    }
}
