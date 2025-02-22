package org.knock.knock_back.service.crawling;

import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.knock.knock_back.component.util.maker.WebDriverUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Objects;

@Service
public interface CrawlingInterface {

    Logger logger = LoggerFactory.getLogger(CrawlingInterface.class);

    void addNewIndex();

    class ElementExtractor implements Runnable {

        private final String urlPath;
        private final String cssQuery;
        private final ThreadLocal<WebDriver> driverThreadLocal = ThreadLocal.withInitial(WebDriverUtil::getChromeDriver);

        @Getter
        private Elements elements;
        @Getter
        private WebDriver driver;

        public ElementExtractor(String urlPath, String cssQuery) {
            this.urlPath = urlPath;
            this.cssQuery = cssQuery;
        }

        public void setUpDriver()
        {
            System.out.println("==================Setup Driver==============");
            driver = driverThreadLocal.get();
            if (urlPath == null) {
                logger.error("[{}]", "urlPath is null");
                return;
            }

            driver.navigate().to(urlPath);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        }

        public void preparePage(WebDriver driver, String[] names)
        {
            try {

                System.out.println("========================prepare Page=================");
                Class<?> clazz = Class.forName("org.knock.knock_back.component.util.NextBtnWithCssSelector");
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                Object instance = constructor.newInstance();

                Method method = clazz.getMethod(names[0], WebDriver.class, String.class);

                method.invoke(instance, driver, names[1]);

            } catch (Exception e) {
                logger.error("Error in preparePage: {}", e.getMessage(), e);
            }
        }

        @Override
        public void run() {

            System.out.println("RUN");
            Document urlDoc;

            synchronized (this) {
                urlDoc = Jsoup.parse(Objects.requireNonNull(driver.getPageSource()));
            }

            synchronized (this) {
                System.out.println(urlDoc);
                System.out.println("=============================");
                elements = urlDoc.select(cssQuery);
                System.out.println(cssQuery);
                System.out.println("=================================");
                System.out.println(elements);
                System.out.println("===============================");
            }
        }
    }
}
