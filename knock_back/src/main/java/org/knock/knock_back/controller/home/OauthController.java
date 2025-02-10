package org.knock.knock_back.controller.home;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    public void socialLoginType(@PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
        oauthService.request(socialLoginType);
    }

    /**
     * SSO 요청 후 Refresh 받는 callback Controller
     * 각 SocialLoginType 별 반환 값을 받은 뒤 service 계층에 전달한다.
     * @param socialLoginType : 로그인할 SSO Type (Google, NAVER, KAKAO)
     * @param code : SSO 요청 후 받은 반환 값인 AccessToken
     * @param httpServletResponse : 반환 될 response 객체
     * @return token : response 객체에 refresh 토큰 담아 반환
     */
    @GetMapping(value = "/{socialLoginType}/callback")
    @ResponseBody
    public ResponseEntity<?> callback(@PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
                                   @RequestParam(name = "code") String code, HttpServletResponse httpServletResponse) throws IOException {

        // refreshToken
        String refreshToken = oauthService.requestAccessToken(socialLoginType, code);

        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
//        refreshTokenCookie.setHttpOnly(true);
        // TODO : HTTPS 빌드 이후 true로 변경
//        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30);
        refreshTokenCookie.setAttribute("SameSite", "None");

        httpServletResponse.addCookie(refreshTokenCookie);

        System.out.println("TOKEN : " + refreshTokenCookie.getValue());
        httpServletResponse.sendRedirect("http://localhost:3000/login/auth");

        return ResponseEntity.ok().build();

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
        System.out.println(request.getHeader("X-AUTH-TOKEN"));
        System.out.println(request.getHeader("Authorization"));
        System.out.println("==========");

        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아오기.

            String accessToken = jwtTokenProvider.generateAccessToken(jwtTokenProvider.getUserDetails(token));

            return ResponseEntity.ok(accessToken);

        }

        return ResponseEntity.badRequest().build();

    }
}
