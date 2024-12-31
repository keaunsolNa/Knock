package org.zerock.knock.service.crawling;

import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
public interface CrawlingInterface {

    Logger logger = LoggerFactory.getLogger(CrawlingInterface.class);

    void addNewBrands();

    @Async
    class ElementExtractor implements Runnable {

        private final String urlPath;
        private final ThreadLocal<WebDriver> driverThreadLocal;
        private final String cssQuery;
        private final String nextBtnCss;

        @Getter
        private Elements elements;

        public ElementExtractor(String urlPath, ThreadLocal<WebDriver> driverThreadLocal, String cssQuery, String nextBtnCss) {
            this.urlPath = urlPath;
            this.driverThreadLocal = driverThreadLocal;
            this.cssQuery = cssQuery;
            this.nextBtnCss = nextBtnCss;
        }

        @Override
        public void run() {

            WebDriver driver = driverThreadLocal.get();

            if (urlPath == null) {
                logger.error("[{}]", "urlPath is null");
                return;
            }

            driver.navigate().to(urlPath);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

            if (nextBtnCss != null) {

                try {

                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 최대 10초 대기

                    while (true)
                    {
                        WebElement nextBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(nextBtnCss)));

                        if (nextBtn.isDisplayed())
                        {
                            nextBtn.click();
                            wait.until(ExpectedConditions.stalenessOf(nextBtn));
                        }
                        else
                        {
                            break;
                        }
                    }
                }
                catch (Exception e)
                {
                    logger.debug("오류 발생: {}", e.getMessage());
                }
            }
            Document urlDoc;

            synchronized (this) {
                urlDoc = Jsoup.parse(Objects.requireNonNull(driver.getPageSource()));
            }

            synchronized (this) {
                elements = urlDoc.select(cssQuery);
            }
        }
    }
}
