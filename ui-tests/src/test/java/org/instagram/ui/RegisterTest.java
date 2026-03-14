package org.instagram.ui;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegisterTest extends BaseTest{

    @Test
    void shouldShowRegisterForm(){
        driver.get("http://localhost:5173/register");
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

    @Test
    //Trazi pokrenut auth-service
    @Disabled("Requires auth-service to be running")
    void shouldRegisterSuccessfully(){
        driver.get("http://localhost:5173/register");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement emailInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//input[@placeholder='E-mail']")));

        WebElement usernameInput = driver.findElement(
                By.xpath("//input[@placeholder='Username']"));

        WebElement passwordInput = driver.findElement(
                By.xpath("//input[@placeholder='Password']")
        );

        WebElement registerButton = driver.findElement(
                By.xpath("//button[@type='submit']")
        );

        emailInput.sendKeys("test@test.com");
        usernameInput.sendKeys("testuser");
        passwordInput.sendKeys("testpassword");
        registerButton.click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    void shouldNavigateToLogin() {
        driver.get("http://localhost:5173/register");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement loginLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.linkText("Login")));

        loginLink.click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }
}
