package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setupAll() {
        // Автоматически скачивает и настраивает нужный ChromeDriver под твою ОС[cite: 78, 79]
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless"); // Включает headless-режим (обязательно для CI-серверов без графического интерфейса)[cite: 80]
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
        driver.get("http://localhost:9999"); // Запускаем локальный сервис

        // Заполняем поле "Фамилия и имя" (используем вложенный селектор по data-test-id)[cite: 58, 95]
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Василий Васильев");

        // Заполняем поле "Мобильный телефон"
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");

        // Кликаем по чекбоксу согласия на обработку персональных данных
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();

        // Нажимаем кнопку "Продолжить" / "Отправить"
        driver.findElement(By.cssSelector("[data-test-id='submit']")).click();

        // Получаем текст успешного сообщения и проверяем его
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена!", text.trim());
    }
}