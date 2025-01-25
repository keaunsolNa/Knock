package org.zerock.knock.controller.home;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.zerock.knock.dto.Enum.SocialLoginType;
import org.zerock.knock.service.layerClass.OauthService;

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
    private static final Logger logger = LoggerFactory.getLogger(OauthController.class);

    /**
     * SSO LOGIN 시도 시 인입되는 페이지. 각 요청 별 enum 타입으로 Service request 시행
     * @param socialLoginType : 로그인할 SSO Type (Google, NAVER, KAKAO)
     */
    @GetMapping(value = "/{socialLoginType}")
    public void socialLoginType(@PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
        logger.info("{} Social Login", socialLoginType);
        oauthService.request(socialLoginType);
    }

    /**
     * SSO 요청 후 AccessCode 받는 callback Controller
     * 각 SocialLoginType 별 반환 값을 받은 뒤 service 계층에 전달한다.
     * @param socialLoginType : 로그인할 SSO Type (Google, NAVER, KAKAO)
     * @param code : SSO 요청 후 받은 반환 값인 AccessToken
     */
    @GetMapping(value = "/{socialLoginType}/callback")
    public String callback(@PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
                           @RequestParam(name = "code") String code)
    {

        String token = oauthService.requestAccessToken(socialLoginType, code);

        // TODO : 랜딩페이지로 redirect
        return null;
    }
}
