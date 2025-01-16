package org.zerock.knock.service.layerClass;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zerock.knock.dto.Enum.Role;
import org.zerock.knock.dto.Enum.SocialLoginType;
import org.zerock.knock.dto.document.user.SSO_USER_INDEX;
import org.zerock.knock.repository.user.SSOUserRepository;
import org.zerock.knock.service.oAuth.SocialOauth;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final List<SocialOauth> socialOauthList;
    private final HttpServletResponse response;
    private static final Logger logger = LoggerFactory.getLogger(OauthService.class);
    private final SSOUserRepository userRepository;

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

    public void requestAccessToken(SocialLoginType socialLoginType, String code) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);

        String callBackResponse = socialOauth.requestAccessToken(code);
        ObjectMapper mapper = new ObjectMapper();

        // JSON 파싱
        JsonNode jsonNode = null;
        try
        {
            jsonNode = mapper.readTree(callBackResponse);
        }
        catch (JsonProcessingException e)
        {
            logger.debug(e.getMessage());
        }

        // access_token 추출
        assert jsonNode != null;
        String accessToken = jsonNode.get("access_token").asText();

        // UserInfo 요청 후 해당 ID 없다면 인덱싱
        requestGoogleAccountProfile(socialLoginType, accessToken);

    }

    private SocialOauth findSocialOauthByType(SocialLoginType socialLoginType) {
        return socialOauthList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }

    public void requestGoogleAccountProfile(SocialLoginType socialLoginType, final String accessToken) {

        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);

        String userInfo = socialOauth.requestUserInfo(accessToken);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonNode = null;
        try
        {
            jsonNode = mapper.readTree(userInfo);
        }
        catch (JsonProcessingException e)
        {
            logger.debug(e.getMessage());
        }

        assert jsonNode != null;
        String id = jsonNode.get("sub").asText();

        if (userRepository.findById(id).isEmpty())
        {

            SSO_USER_INDEX ssoUserIndex = SSO_USER_INDEX.builder()
                    .id(id)
                    .name(jsonNode.get("name").asText())
                    .email(jsonNode.get("email").asText())
                    .picture(jsonNode.get("picture").asText())
                    .loginType(SocialLoginType.GOOGLE)
                    .role(Role.USER)
                    .build();

            userRepository.save(ssoUserIndex);

        }
        else
        {
            SSO_USER_INDEX existingUser = userRepository.findById(id).get();

            SSO_USER_INDEX updatedUser = existingUser.update(
                    jsonNode.get("name").asText(),
                    jsonNode.get("picture").asText()
            );

            userRepository.save(updatedUser);

        }

        logger.info("LOGIN : [{}]", userRepository.findById(id).get().getName());

    }
}
