package org.zerock.knock.service.crawling.movie;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.zerock.knock.service.crawling.CrawlingInterface;

import java.util.Objects;

@Service
public class MegaBox implements CrawlingInterface {


    @Override
    public void addNewBrands() {

        logger.info("MEGA_BOX START");

        // Webdriver initialize
        String url = "https://www.megabox.co.kr/movie/comingsoon";
        String cssQuery = "ol#movieList > li";

        ElementExtractor extractor = new ElementExtractor(url, cssQuery);

        extractor.setUpDriver();
        extractor.preparePage("org.zerock.knock.component.util.NextBtnWithCssSelector", "nextBtn", ".btn-more");
        extractor.run();

        Elements elements = extractor.getElements();

        for (Element element : elements)
        {
            Elements titleElements = element.select("div.tit-area > p.tit");

            if (!titleElements.isEmpty())
            {
                String title = Objects.requireNonNull(titleElements.first()).text();
                logger.info("제목 {}", title);
            }

            Elements dateElements = element.select("div.rate-date > span.date");
            if (!dateElements.isEmpty())
            {
                String date = Objects.requireNonNull(dateElements.first()).text().replace("개봉일 ", "");
                logger.info("개봉일 {}", date);
            }
            logger.info("NEXT");
        }

        logger.info("EXTRACTOR END");

    }
}
