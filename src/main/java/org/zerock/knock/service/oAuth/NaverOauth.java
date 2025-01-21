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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NaverOauth implements SocialOauth {

    @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private String NAVER_BASE_URL;
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String NAVER_CALLBACK_URL;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String NAVER_TOKEN_URI;
    @Value("${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
    private String NAVER_GRANT_TYPE;
    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String NAVER_USER_INFO_URI;

    private final SSOUserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(GoogleOauth.class);

    @Override
    public String getOauthRedirectURL() {

        Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", NAVER_CLIENT_ID);
        params.put("redirect_uri", NAVER_CALLBACK_URL);
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();
        params.put("state", state);
        params.put("grant_type", NAVER_GRANT_TYPE);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return NAVER_BASE_URL + "?" + parameterString;
    }

    @Override
    public String requestAccessToken(String code) {

        RestTemplate restTemplate = new RestTemplate();

        String apiURL = NAVER_TOKEN_URI +
                "?grant_type=" + NAVER_GRANT_TYPE +
                "&client_id=" + NAVER_CLIENT_ID +
                "&client_secret=" + NAVER_CLIENT_SECRET +
                "&redirect_uri=" + NAVER_CALLBACK_URL +
                "&code=" + code;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try
        {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiURL,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK)
            {
                return responseEntity.getBody();
            }
            else
            {
                logger.error("Failed to retrieve access token: {}", responseEntity.getBody());
            }

        }
        catch (Exception e)
        {
            logger.error("Error during access token request: {}", e.getMessage());
        }

        return "NAVER 로그인 요청 처리 실패";
    }

    @Override
    public void requestUserInfo(String accessToken) {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        final HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        String userInfo = restTemplate.exchange(NAVER_USER_INFO_URI, HttpMethod.GET, httpEntity, String.class)
                .getBody();

        JsonNode jsonNode = null;
        try
        {
            jsonNode = mapper.readTree(userInfo).get("response");
        }
        catch (JsonProcessingException e)
        {
            logger.debug(e.getMessage());
        }

        assert jsonNode != null;
        String id = jsonNode.get("id").asText();

        if (userRepository.findById(id).isEmpty())
        {

            RandomNickNameMaker randomNickNameMaker = new RandomNickNameMaker();

            SSO_USER_INDEX ssoUserIndex = SSO_USER_INDEX.builder()
                    .id(id)
                    .name(jsonNode.get("name").asText())
                    .email(jsonNode.get("email").asText())
                    .nickName(randomNickNameMaker.makeRandomNickName())
                    .picture(jsonNode.get("profile_image").asText())
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
