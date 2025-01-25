package org.knock.knock_back.service.layerClass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.knock.knock_back.service.crawling.movie.KOFIC;

@Service
public class KOFICService {

    private final KOFIC kofic;

    @Autowired
    public KOFICService(KOFIC kofic) {
        this.kofic = kofic;
    }

    @Async
    public void startCrawling() {
        kofic.requestAPI();
    }

}
