package org.zerock.knock.controller.crawling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.knock.service.LayerClass.CategoryInitializer;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_ONE_INDEX;
import org.zerock.knock.service.LayerClass.KOFICService;
import org.zerock.knock.service.crawling.movie.MegaBox;

@RestController
@RequestMapping("/api/crawling")
public class CrawlingController {

    private final CategoryInitializer categoryInitializer;
    private final MegaBox megaBox;
    private final KOFICService koficService;

    @Autowired
    public CrawlingController(MegaBox megaBox, CategoryInitializer categoryInitializer, KOFICService koficService) {
        this.megaBox = megaBox;
        this.categoryInitializer = categoryInitializer;
        this.koficService = koficService;
    }

    @GetMapping("/megaBox")
    public String crawlingMegaBox() {
        megaBox.addNewBrands();
        return "Crawling Completed!";
    }

    @GetMapping("/kofic")
    public String crawlingKOFIC() {
        koficService.startCrawling();
        return "Crawling Completed!";
    }

    @PostMapping("/categoryInitialize")
    public ResponseEntity<Iterable<CATEGORY_LEVEL_ONE_INDEX>> categoryInitialize() {

        return ResponseEntity.ok(categoryInitializer.initializeCategoryLevelOne());
    }
}
