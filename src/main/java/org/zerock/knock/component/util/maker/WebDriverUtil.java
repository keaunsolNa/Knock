package org.zerock.knock.component.util.maker;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author nks
 * @apiNote WebDriver 설정을 제어하고 생성된 객체를 반환한다.
 */
@Component
public class WebDriverUtil {

    /**
     * 이미지를 Base64로 변환하는 유틸리티 메서드.
     * @return 생성된 WebDriver 객체
     */
    @Bean
    public static WebDriver getChromeDriver() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--log-level=3");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--blink-settings=imagesEnabled=false");

        WebDriver driver = new ChromeDriver(options);

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10000));

        return driver;

    }

}
