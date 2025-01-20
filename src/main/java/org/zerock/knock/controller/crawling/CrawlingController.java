package org.zerock.knock.controller.crawling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    protected final Logger logger = LoggerFactory.getLogger(getClass());

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
    public void crawlingMegaBox() {
        megaBox.addNewIndex();
        logger.info("Crawling MegaBox End");
    }

    /**
     * KOFIC 의 모든 영화 정보를 가져와 Index 저장
     */
    @GetMapping("/kofic")
    public void crawlingKOFIC() {
        koficService.startCrawling();
        logger.info("Crawling KOFIC End");
    }

}
