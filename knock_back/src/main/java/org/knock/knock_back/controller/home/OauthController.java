package org.knock.knock_back.controller.home;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.knock.knock_back.component.config.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.knock.knock_back.dto.Enum.SocialLoginType;
import org.knock.knock_back.service.layerClass.OauthService;

/**
 * @author nks
 * @apiNote SSO Login 시 인입되는 페이지
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OauthController {

    private final OauthService oauthService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * SSO LOGIN 시도 시 인입되는 페이지. 각 요청 별 enum 타입으로 Service request 시행
     * @param socialLoginType : 로그인할 SSO Type (Google, NAVER, KAKAO)
     */
    @GetMapping(value = "/{socialLoginType}")
    public ResponseEntity<Map<String, String>> socialLoginType(@PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {

        return ResponseEntity.ok()
                .body(oauthService.request(socialLoginType));
//        oauthService.request(socialLoginType);
    }

    /**
     * SSO 요청 후 Refresh 받는 callback Controller
     * 각 SocialLoginType 별 반환 값을 받은 뒤 service 계층에 전달한다.
     * @param socialLoginType : 로그인할 SSO Type (Google, NAVER, KAKAO)
     * @param authorizationCode : SSO 요청 후 받은 반환 값인 verify code
     * @param httpServletResponse : 반환 될 response 객체
     * @return token : response 객체에 refresh 토큰 담아 반환
     */
    @GetMapping(value = "/{socialLoginType}/callback")
    @ResponseBody
    public ResponseEntity<Map<String, String>> callback(@PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
                                    @RequestParam(name = "authorizationCode") String authorizationCode, HttpServletResponse httpServletResponse) {

        // refreshToken
        String[] tokens = oauthService.requestAccessToken(socialLoginType, authorizationCode);
        String refreshTokenValue = tokens[0];
        String accessTokenValue = tokens[1];

        Cookie refreshTokenForKnock = new Cookie("refreshTokenForKnock", refreshTokenValue);

        refreshTokenForKnock.setPath("/");
        refreshTokenForKnock.setHttpOnly(true);
        refreshTokenForKnock.setSecure(true);
        refreshTokenForKnock.setMaxAge(7 * 24 * 24 * 30);
//        accessToken.setDomain("203.229.246.216");
        refreshTokenForKnock.setDomain("203.229.246.216");
        refreshTokenForKnock.setAttribute("SameSite", "None");

        httpServletResponse.addCookie(refreshTokenForKnock);

        System.out.println("ACCESS_TOKEN : " + accessTokenValue);
        System.out.println("REFRESH_TOKEN : " + refreshTokenForKnock.getValue());
        String redirectUrl = "http://localhost:3000/movie";

        Map<String, String> response = new HashMap<>();
        response.put("redirect_url", redirectUrl);
        response.put("access_token", accessTokenValue);

        return ResponseEntity.ok(response);

    }

    /**
     * 프론트로부터 refreshToken 받아 accessToken 반환한다.
     * @return token : response 객체에 access 토큰 담아 반환
     */
    @PostMapping(value = "/getAccessToken")
    @ResponseBody
    public ResponseEntity<String> getAccessToken( HttpServletRequest request ) {

        System.out.println(request);

        String token = request.getHeader("X-AUTH-TOKEN");

        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아오기.

            String accessToken = jwtTokenProvider.generateAccessToken(jwtTokenProvider.getUserDetails(token));

            return ResponseEntity.ok(accessToken);

        }

        return ResponseEntity.badRequest().build();

    }
}
