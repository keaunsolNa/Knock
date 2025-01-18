package org.zerock.knock.controller.crawling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.knock.service.layerClass.KOFICService;
import org.zerock.knock.service.crawling.movie.MegaBox;

/**
 * @author nks
 * @apiNote Crawling 요청을 받는 Controller
 */
@RestController
@RequestMapping("/api/crawling")
public class CrawlingController {

    private final MegaBox megaBox;
    private final KOFICService koficService;

    @Autowired
    public CrawlingController(MegaBox megaBox, KOFICService koficService) {
        this.megaBox = megaBox;
        this.koficService = koficService;
    }

    /**
     * MegaBox 의 상영 예정작품들을 가져와 Index 저장
     */
    @GetMapping("/megaBox")
    public String crawlingMegaBox() {
        megaBox.addNewIndex();
        return "Crawling Completed!";
    }

    /**
     * KOFIC 의 모든 영화 정보를 가져와 Index 저장
     */
    @GetMapping("/kofic")
    public String crawlingKOFIC() {
        koficService.startCrawling();
        return "Crawling Completed!";
    }

}
