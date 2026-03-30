package org.instagram.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends BaseTest {

    @Override
    @BeforeEach
    void setUp(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:3000/login");
    }
    @Test
    // Zahteva pokrenut auth-service
    //`@Disabled("Requires auth-service to be running")
    void shouldLoginSuccessfully(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement usernameInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//input[@placeholder='Username']")));

        WebElement passwordInput = driver.findElement(
                By.xpath("//input[@placeholder='Password']"));

        WebElement loginButton = driver.findElement(
                By.xpath("//button[@type='submit']"));

        usernameInput.sendKeys("legolas");
        passwordInput.sendKeys("123456");
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/feed"));
        assertTrue(driver.getCurrentUrl().contains("/feed"));
    }

    @Test
    void shouldShowLoginForm(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement usernameInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//input[@placeholder='Username']")));

        WebElement passwordInput = driver.findElement(
                By.xpath("//input[@placeholder='Password']"));

        WebElement loginButton = driver.findElement(
                By.xpath("//button[@type='submit']"));

        assertTrue(usernameInput.isDisplayed());
        assertTrue(passwordInput.isDisplayed());
        assertTrue(loginButton.isDisplayed());
    }

    @Test
    void shouldNavigateToRegister(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement registerLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//a[@href='/register']")));

        registerLink.click();

        wait.until(ExpectedConditions.urlContains("/register"));
        assertTrue(driver.getCurrentUrl().contains("/register"));
    }
}
