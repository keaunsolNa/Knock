package org.zerock.knock.service.LayerClass;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zerock.knock.dto.Enum.SocialLoginType;
import org.zerock.knock.service.Oauth.SocialOauth;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final List<SocialOauth> socialOauthList;
    private final HttpServletResponse response;
    private static final Logger logger = LoggerFactory.getLogger(OauthService.class);

    public void request(SocialLoginType socialLoginType)
    {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        String redirectURL = socialOauth.getOauthRedirectURL();

        try
        {
            response.sendRedirect(redirectURL);
        }
        catch (IOException e)
        {
            logger.debug(e.getMessage());
        }
    }

    public String requestAccessToken(SocialLoginType socialLoginType, String code) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);

        String answer = socialOauth.requestAccessToken(code);
        ObjectMapper mapper = new ObjectMapper();

        // JSON 파싱
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(answer);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // access_token 추출
        String accessToken = jsonNode.get("access_token").asText();

        return requestGoogleAccountProfile(socialLoginType, accessToken);
    }

    private SocialOauth findSocialOauthByType(SocialLoginType socialLoginType) {
        return socialOauthList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }

    public String requestGoogleAccountProfile(SocialLoginType socialLoginType, final String accessToken) {

        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        String answer = socialOauth.requestUserInfo(accessToken);

        logger.info(answer);
        return null;
    }
}
