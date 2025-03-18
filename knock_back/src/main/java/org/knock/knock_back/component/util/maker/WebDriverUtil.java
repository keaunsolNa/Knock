package org.knock.knock_back.component.util.maker;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author nks
 * @apiNote WebDriver 설정을 제어하고 생성된 객체를 반환한다.
 */
@Slf4j
@Component
public class WebDriverUtil {

    /**
     * ChromeDriver 옵션 지정 및 생성
     * @return 생성된 WebDriver 객체
     */
    @Bean
    public static WebDriver getChromeDriver() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--log-level=3");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--blink-settings=imagesEnabled=false");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("--disable-extensions");  // ✅ 불필요한 확장 비활성화
        options.addArguments("--disable-background-networking");  // ✅ 네트워크 사용 최소화
        options.addArguments("--disable-features=VizDisplayCompositor"); // ✅ GPU 메모리 절약
        options.addArguments("--single-process");  // ✅ 여러 프로세스를 하나로 통합하여 RAM 절약

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);  // 3초 대기 후 다음 요소 탐색


        return driver;

    }

}
