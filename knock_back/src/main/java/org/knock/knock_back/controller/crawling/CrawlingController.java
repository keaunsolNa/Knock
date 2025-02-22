package org.knock.knock_back.controller.crawling;

import org.knock.knock_back.service.crawling.movie.CGV;
import org.knock.knock_back.service.crawling.movie.LOTTE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.knock.knock_back.service.layerClass.KOFICService;
import org.knock.knock_back.service.crawling.movie.MegaBox;

/**
 * @author nks
 * @apiNote Crawling 요청을 받는 Controller
 */
@RestController
@RequestMapping("/api/crawling")
public class CrawlingController {

    private final MegaBox megaBox;
    private final CGV cgv;
    private final LOTTE lotte;
    private final KOFICService koficService;

    @Autowired
    public CrawlingController(MegaBox megaBox, CGV cgv, LOTTE lotte, KOFICService koficService) {
        this.megaBox = megaBox;
        this.cgv = cgv;
        this.lotte = lotte;
        this.koficService = koficService;
    }

    /**
     * MegaBox 의 상영 예정작품들을 가져와 Index 저장
     */
    @GetMapping("/megaBox")
    public void crawlingMegaBox() {
        megaBox.addNewIndex();
    }

    /**
     * CGV 상영 예정작품들을 가져와 Index 저장
     */
    @GetMapping("/cgv")
    public void crawlingCgv() { cgv.addNewIndex(); }

    /**
     * Lotte 상영 예정작품들을 가져와 Index 저장
     */
    @GetMapping("/lotte")
    public void crawlingLotte() { lotte.addNewIndex(); }
    /**
     * KOFIC 의 모든 영화 정보를 가져와 Index 저장
     */
    @GetMapping("/kofic")
    public void crawlingKOFIC() {
        koficService.startCrawling();
    }

}
