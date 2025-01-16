package org.zerock.knock.controller.home;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
/**
 * @author nks
 * @apiNote 메인 페이지 요청 시 진입되는 Controller
 */
@Controller
public class HomeController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 인덱스 페이지
     * @return redirect URI
     */
    @GetMapping("/")
    @ResponseBody
    public String index()
    {
        return "로그인을 하세요 <a href='/oauth2/authorization/google'>구글로 로그인</a>";
    }

}
