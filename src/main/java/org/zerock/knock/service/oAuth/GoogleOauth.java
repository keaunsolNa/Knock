package org.zerock.knock.service.oAuth;

import com.fasterxml.jackson.core.JsonProcessingException;
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

@Component
@RequiredArgsConstructor
public class GoogleOauth implements SocialOauth
{

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

    @Override
    public String requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("client_secret", GOOGLE_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_CALLBACK_URL);
        params.put("grant_type", GOOGLE_GRANT_TYPE);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL, params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK)
        {
            return responseEntity.getBody();
        }
        return "구글 로그인 요청 처리 실패";

    }

    @Override
    public void requestUserInfo(String accessToken)
    {

        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        final HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        String userInfo = restTemplate.exchange(GOOGLE_USER_INFO_URI, HttpMethod.GET, httpEntity, String.class)
                .getBody();

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
