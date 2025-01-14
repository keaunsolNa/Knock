package org.zerock.knock.controller.home;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/")
    @ResponseBody
    public String index()
    {
        return "로그인을 하세요 <a href='/oauth2/authorization/google'>구글로 로그인</a>";
    }

    @GetMapping("/home")
    @ResponseBody
    public String home(@AuthenticationPrincipal OidcUser principal)
    {

        logger.info("In home");
        logger.info("[{}]", principal);
        return "환영합니다, " + principal.getFullName();
    }

    @RestController
    @RequestMapping(value = "/login/oauth2", produces = "application/json")
    @RequiredArgsConstructor
    public class LoginCtrl {
        @GetMapping("/code/{company}")
        public void googleLogin(@RequestParam String code, @PathVariable String company) {

            logger.info("{} Code : ", code);
            logger.info("{} company : ", company);
        }
    }
}
