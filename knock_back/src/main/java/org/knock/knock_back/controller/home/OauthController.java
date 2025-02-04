package org.knock.knock_back.controller.home;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.knock.knock_back.dto.Enum.SocialLoginType;
import org.knock.knock_back.service.layerClass.OauthService;
import org.springframework.web.servlet.view.RedirectView;

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

    /**
     * SSO LOGIN 시도 시 인입되는 페이지. 각 요청 별 enum 타입으로 Service request 시행
     * @param socialLoginType : 로그인할 SSO Type (Google, NAVER, KAKAO)
     */
    @GetMapping(value = "/{socialLoginType}")
    public void socialLoginType(@PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
        oauthService.request(socialLoginType);
    }

    /**
     * SSO 요청 후 AccessCode 받는 callback Controller
     * 각 SocialLoginType 별 반환 값을 받은 뒤 service 계층에 전달한다.
     * @param socialLoginType : 로그인할 SSO Type (Google, NAVER, KAKAO)
     * @param code : SSO 요청 후 받은 반환 값인 AccessToken
     */
    @GetMapping(value = "/{socialLoginType}/callback")
    @ResponseBody
    public RedirectView callback(@PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
                                 @RequestParam(name = "code") String code)
    {

        String favoriteCategory = oauthService.requestAccessToken(socialLoginType, code);

        System.out.println(favoriteCategory);
        RedirectView redirectView = new RedirectView();
        // TODO : front domain으로 반환 및 redirectView 관련 추가
        redirectView.setUrl("http://localhost:3000/movie");

        return redirectView;
    }
}
