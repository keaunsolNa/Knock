package org.zerock.knock.controller.crawling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.knock.component.util.CategoryInitializer;
import org.zerock.knock.service.crawling.CrawlingInterface;
import org.zerock.knock.service.crawling.movie.KOFIC;

@RestController
@RequestMapping("/api/crawling")
public class CrawlingController {

    private final CrawlingInterface crawlingService;
    private final CategoryInitializer categoryInitializer;
    private final KOFIC kofic;

    @Autowired
    public CrawlingController(@Qualifier("megaBox") CrawlingInterface crawlingService, CategoryInitializer categoryInitializer, KOFIC kofic) {
        this.crawlingService = crawlingService;
        this.categoryInitializer = categoryInitializer;
        this.kofic = kofic;
    }

    @GetMapping("/megaBox")
    public String crawlingMegaBox() {
        crawlingService.addNewBrands();
        return "Crawling Completed!";
    }

    @GetMapping("/kofic")
    public String crawlingKOFIC() {
        kofic.requestAPI();
        return "Crawling Completed!";
    }

    @PostMapping("/categoryInitialize")
    public String categoryInitialize() {
        categoryInitializer.initializeCategoryLevelTwo();
        categoryInitializer.initializeCategoryLevelOne();
        return "END";
    }
}
