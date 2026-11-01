package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Test
    void shouldSuccessfullySubmitRequest() {
        driver.get("http://localhost:9999");

        // Заполняем поле имени (разрешены русские буквы, пробелы и дефисы)
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванов");

        // Заполняем поле телефона (строго формат + и 11 цифр)
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");

        // Кликаем по чекбоксу соглашения
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();

        // Кликаем по кнопке отправки (в бандле используется view="extra", ищем по тегу button)
        driver.findElement(By.cssSelector("button.button")).click();

        // Ждем появления блока успешной отправки с точным data-test-id из кода приложения
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement successBlock = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='order-success']"))
        );

        // Проверяем текст успешной отправки
        String text = successBlock.getText();
        assertTrue(text.contains("Ваша заявка успешно отправлена!"), "Текст уведомления не совпадает: " + text);
    }
}