package org.instagram.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    void setUp(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:3000/login");
        login();
    }

    private void login() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // ? pove?aj timeout
        WebElement usernameInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//input[@placeholder='Username']")));
        usernameInput.sendKeys("legolas");
        driver.findElement(By.xpath("//input[@placeholder='Password']"))
                .sendKeys("123456");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("/feed"));

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.className("ig-sidebar__nav")));
    }
    @AfterEach
    void tearDown(){
        if(driver != null){
            driver.quit();
        }
    }
}
