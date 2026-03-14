package org.instagram.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SettingsTest extends BaseTest{

    @Test
    void shouldShowSettingsForm(){
        driver.get("http://localhost:5173/settings");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement form = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-settings-form")
                )
        );

        assertTrue(form.isDisplayed());
    }

    @Test
    void shouldShowBlockedAccounts(){
        driver.get("http://localhost:5173/settings");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement blockedList = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-settings-blocked-list")
                )
        );
        assertTrue(blockedList.isDisplayed());

        List<WebElement> blockedItems = driver.findElements(By.className("ig-settings-blocked-item"));
        assertFalse(blockedItems.isEmpty());
    }

    @Test
    void shouldUnblockUser(){
        driver.get("http://localhost:5173/settings");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        List<WebElement> unblockButtons = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.className("ig-settings-unblock-btn")
                )
        );
        int initialCount = unblockButtons.size();
        unblockButtons.get(0).click();

        List<WebElement> updatedItems = driver.findElements(By.className("ig-settings-blocked-item"));
        assertTrue(updatedItems.size()<initialCount);
    }

    @Test
    void shouldSaveShanges(){
        driver.get("http://localhost:5173/settings");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement submitButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-settings-submit")
                )
        );

        submitButton.click();

        WebElement savedMessage = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-settings-saved")
                )
        );
        assertTrue(savedMessage.isDisplayed());
        assertTrue(savedMessage.getText().contains("Changes saved"));
    }
}
