package org.zerock.knock.service.crawling;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zerock.knock.component.util.WebDriverUtil;

import java.util.Objects;

@Service
public class MegaBox implements  CrawlingInterface {

    private static final Logger logger = LoggerFactory.getLogger(MegaBox.class);

    @Override
    public void addNewBrands() {

        logger.info("MEGA_BOX START");

        // Webdriver initialize
        String url = "https://www.megabox.co.kr/movie/comingsoon";

        ThreadLocal<WebDriver> driverThreadLocal = ThreadLocal.withInitial(WebDriverUtil::getChromeDriver);
        ElementExtractor extractor = new ElementExtractor(url, driverThreadLocal, "ol#movieList > li", ".btn-more");
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
