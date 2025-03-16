package org.knock.knock_back.component.util.maker;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.UUID;

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

        String uniqueTempDir = "/tmp/chrome_user_data_" + UUID.randomUUID();
        Path tempDirPath = Paths.get(uniqueTempDir);
        try {
            Files.createDirectories(tempDirPath);
            log.info("✅ Created user-data-dir: {}", uniqueTempDir);  // 디렉토리 생성 로그
        } catch (Exception e) {
            log.error("Failed to create temp directory for Chrome user data: {}", e.getMessage());
        }

        // 기존 크롬 프로세스 종료 (충돌 방지)
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("pkill", "-f", "chrome");
            processBuilder.start();
            log.info("✅ Attempted to kill existing Chrome processes.");
        } catch (Exception e) {
            log.warn("Failed to kill existing Chrome processes: {}", e.getMessage());
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--log-level=3");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--blink-settings=imagesEnabled=false");
        options.addArguments("--disable-notifications");

        // 🔹 고유한 user-data-dir 설정
        options.addArguments("--user-data-dir=" + uniqueTempDir);
        log.info("✅ Chrome option set: --user-data-dir={}", uniqueTempDir);

        ChromeDriverService service = null;

        try
        {
            service = new ChromeDriverService.Builder()
                    .usingDriverExecutable(new File("/app/.cache/selenium/chromedriver")) // Heroku에 맞게 ChromeDriver 경로 수정
                    .usingAnyFreePort()
                    .build();

        }
        finally {
            assert service != null;
            service.close();
        }


        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(100));

        log.info("✅ ChromeDriver started successfully!");

        return driver;

    }

}
