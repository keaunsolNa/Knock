package org.zerock.knock.service.Oauth;

import org.zerock.knock.dto.Enum.SocialLoginType;

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
