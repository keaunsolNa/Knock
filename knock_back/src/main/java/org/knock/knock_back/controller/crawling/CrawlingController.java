package org.knock.knock_back.controller.crawling;

import org.knock.knock_back.service.crawling.CrawlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.knock.knock_back.service.layerClass.KOFICService;

/**
 * @author nks
 * @apiNote Crawling 요청을 받는 Controller
 */
@RestController
@RequestMapping("/api/crawling")
public class CrawlingController {

    private final KOFICService koficService;
    private final CrawlingService crawlingService;

    @Autowired
    public CrawlingController(KOFICService koficService, CrawlingService crawlingService) {
        this.koficService = koficService;
        this.crawlingService = crawlingService;
    }

    /**
     * 상영 예정작품들을 가져와 Index 저장
     */
    @GetMapping("/{source}")
    public ResponseEntity<String> crawl(@PathVariable String source) {

        try
        {
            crawlingService.startCrawling(source);
            return ResponseEntity.ok("Crawling started for " + source);
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * KOFIC 의 모든 영화 정보를 가져와 Index 저장
     */
    @GetMapping("/kofic")
    public void crawlingKOFIC() {
        koficService.startCrawling();
    }

}
