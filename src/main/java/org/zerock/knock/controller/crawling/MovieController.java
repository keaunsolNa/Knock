package org.zerock.knock.controller.crawling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.knock.service.crawling.CrawlingInterface;

@RestController
@RequestMapping("/movies/crawl")
public class MovieController {

    private final CrawlingInterface crawlingService;

    @Autowired
    public MovieController(@Qualifier("megaBox") CrawlingInterface crawlingService) {
        this.crawlingService = crawlingService;
    }

    @GetMapping("/megaBox")
    public String startCrawling() {
        crawlingService.addNewBrands(); // 인터페이스 메서드 호출
        return "Crawling Completed!";
    }
}