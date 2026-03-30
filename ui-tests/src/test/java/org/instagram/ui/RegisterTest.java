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

public class RegisterTest extends BaseTest{

    @Override
    @BeforeEach
    void setUp(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:3000/register");
    }

    @Test
    void shouldShowRegisterForm(){
        driver.get("http://localhost:3000/register");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement emailInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//input[@placeholder='E-mail']")));

        WebElement usernameInput = driver.findElement(
                By.xpath("//input[@placeholder='Username']"));

        WebElement passwordInput = driver.findElement(
                By.xpath("//input[@placeholder='Password']"));

        WebElement registerButton = driver.findElement(
                By.xpath("//button[@type='submit']"));

        assertTrue(emailInput.isDisplayed());
        assertTrue(usernameInput.isDisplayed());
        assertTrue(passwordInput.isDisplayed());
        assertTrue(registerButton.isDisplayed());
    }

    //Trazi pokrenut auth-service
    //@Disabled("Requires auth-service to be running")
    @Test
    void shouldRegisterSuccessfully(){
        driver.get("http://localhost:3000/register");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        String unique = String.valueOf(System.currentTimeMillis());

        WebElement emailInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//input[@placeholder='E-mail']")));

        driver.findElement(By.xpath("//input[@placeholder='Username']"))
                .sendKeys("test" + unique);
        driver.findElement(By.xpath("//input[@placeholder='Password']"))
                .sendKeys("testpassword");
        emailInput.sendKeys("test" + unique + "@test.com");

        driver.findElement(By.xpath("//button[@type='submit']")).click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/feed"),
                ExpectedConditions.urlContains("/login")
        ));
        assertTrue(
                driver.getCurrentUrl().contains("/feed") ||
                        driver.getCurrentUrl().contains("/login")
        );
    }

    @Test
    void shouldNavigateToLogin() {
        driver.get("http://localhost:3000/register");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement loginLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//a[@href='/login']")));

        loginLink.click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }
}
