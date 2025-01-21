package org.zerock.knock.service.oAuth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.zerock.knock.component.util.RandomNickNameMaker;
import org.zerock.knock.dto.Enum.Role;
import org.zerock.knock.dto.Enum.SocialLoginType;
import org.zerock.knock.dto.document.user.SSO_USER_INDEX;
import org.zerock.knock.repository.user.SSOUserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author nks
 * @apiNote Google SSO Login API
 */
@Component
@RequiredArgsConstructor
public class GoogleOauth implements SocialOauth
{

    // application.yml
    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String GOOGLE_BASE_URL;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_CALLBACK_URL;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String GOOGLE_TOKEN_URL;
    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String GOOGLE_USER_INFO_URI;
    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String GOOGLE_GRANT_TYPE;

    private final SSOUserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(GoogleOauth.class);

    /**
     * controller 에서 요청을 받을 경우 Google SSO 요청을 하는 페이지 GET 방식 이동 한다.
     * @return Request URI
     */
    @Override
    public String getOauthRedirectURL() {

        Map<String, Object> params = new HashMap<>();
        params.put("scope", "openid https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email");
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_CALLBACK_URL);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return GOOGLE_BASE_URL + "?" + parameterString;
    }

    /**
     * Get 요청 이후 유저가 로그인 한 후, callback page 에서 받은 verify code 통해 accessToken 요청한다.
     * @param code : verify code
     * @return AccessToken / RuntimeException
     */
    @Override
    public String requestAccessToken(String code) {

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("client_secret", GOOGLE_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_CALLBACK_URL);
        params.put("grant_type", GOOGLE_GRANT_TYPE);

        try {

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL, params, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK)
            {
                return responseEntity.getBody();
            }
            else
            {
                logger.error("Failed to retrieve Google token: {}", responseEntity.getStatusCode());
                throw new RuntimeException("Failed to retrieve Google token");
            }
        }
        catch (Exception e)
        {
            logger.error("Exception during Kakao token retrieval: ", e);
            throw new RuntimeException("Exception during Kakao token retrieval", e);
        }

    }

    /**
     * AccessToken 받은 후 user 정보를 요청하는 API
     * userInfo 를 받은 경우, 해당하는 id가 sso-user-index 에 있다면 update, 없다면 insert 수행
     * @param accessToken : 전달받은 AccessToken
     */
    @Override
    public void requestUserInfo(String accessToken)
    {

        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        JsonNode jsonNode;

        try {

            // GET 요청 전송
            ResponseEntity<String> responseEntity = restTemplate.exchange(GOOGLE_USER_INFO_URI, HttpMethod.GET, httpEntity, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK)
            {
                jsonNode = mapper.readTree(responseEntity.getBody());
            }
            else
            {
                logger.error("Failed to retrieve Google user info: {}", responseEntity.getStatusCode());
                throw new RuntimeException("Failed to retrieve Google user info");
            }

        }
        catch (Exception e)
        {
            logger.error("Exception during Google user info retrieval: ", e);
            throw new RuntimeException("Exception during Google user info retrieval", e);
        }

        assert jsonNode != null;
        String id = jsonNode.get("sub").asText();

        if (userRepository.findById(id).isEmpty())
        {

            RandomNickNameMaker randomNickNameMaker = new RandomNickNameMaker();

            SSO_USER_INDEX ssoUserIndex = SSO_USER_INDEX.builder()
                    .id(id)
                    .name(jsonNode.get("name").asText())
                    .email(jsonNode.get("email").asText())
                    .nickName(randomNickNameMaker.makeRandomNickName())
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
                    jsonNode.get("email").asText(),
                    jsonNode.get("picture").asText()
            );

            userRepository.save(updatedUser);

        }

        logger.info("LOGIN : [{}]", userRepository.findById(id).get().getName());
    }
}
