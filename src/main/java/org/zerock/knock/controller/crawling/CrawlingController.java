package org.zerock.knock.controller.crawling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.knock.component.util.CategoryInitializer;
import org.zerock.knock.service.crawling.movie.KOFIC;
import org.zerock.knock.service.crawling.movie.MegaBox;

@RestController
@RequestMapping("/api/crawling")
public class CrawlingController {

    private final CategoryInitializer categoryInitializer;
    private final MegaBox megaBox;
    private final KOFIC kofic;

    @Autowired
    public CrawlingController(MegaBox megaBox, CategoryInitializer categoryInitializer, KOFIC kofic) {
        this.megaBox = megaBox;
        this.categoryInitializer = categoryInitializer;
        this.kofic = kofic;
    }

    @GetMapping("/megaBox")
    public String crawlingMegaBox() {
        megaBox.addNewBrands();
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
