package org.knock.knock_back.service.crawling.musical;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.knock.knock_back.service.crawling.common.CrawlingInterface;

import java.util.Objects;

public class InterPark implements CrawlingInterface {

    @Override
    public void addNewIndex() {

        // Webdriver initialize
        String url = "https://ticket.interpark.com/webzine/paper/TPNoticeList.asp?tid1=in_scroll&tid2=ticketopen&tid3=board_main&tid4=board_main";
        String cssQuery = "div.section_notice > div.board > div.list > div.table > table > tbody > tr";


        ElementExtractor extractor = new ElementExtractor(url, cssQuery);

        extractor.setUpDriver();
        WebDriver driver = extractor.getDriver();
        driver.switchTo().frame(driver.findElement(By.cssSelector("iframe")));
//        extractor.preparePage("org.zerock.knock.component.util.NextBtnWithCssSelector", "nextBtn", "a[href=\"javascript:ListSearch(34,'TICKET','1');\"]");
        extractor.run();

        Elements elements = extractor.getElements();

        for (Element element : elements)
        {
            Elements titleElements = element.select("tr > td.subject > a");

            if (!titleElements.isEmpty())
            {
                String title = Objects.requireNonNull(titleElements.first()).text();
                logger.info("제목 {}", title);
            }

            Elements dateElements = element.select("tr > td.date");
            if (!dateElements.isEmpty())
            {
                String date = Objects.requireNonNull(dateElements.first()).text();
                date = date.substring(0, date.indexOf("("));
                logger.info("개봉일 {}", date);
            }
            logger.info("NEXT");
        }

        logger.info("EXTRACTOR END");
    }
}
