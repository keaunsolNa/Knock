package org.zerock.knock.component.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class WebDriverUtil {

    @Bean
    public static WebDriver getChromeDriver() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
/*        options.addArguments("--disable-gpu");*/
        options.addArguments("--log-level=3");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--blink-settings=imagesEnabled=false");

        WebDriver driver = new ChromeDriver(options);

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10000));

        return driver;

    }

}
