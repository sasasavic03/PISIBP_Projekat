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
        driver.get("http://localhost:3000/settings");
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
        driver.get("http://localhost:3000/settings");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // sa?ekaj da se stranica u?ita
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.className("ig-settings-blocked")));

        List<WebElement> blockedItems = driver.findElements(By.className("ig-settings-blocked-item"));
        if (!blockedItems.isEmpty()) {
            WebElement blockedList = driver.findElement(By.className("ig-settings-blocked-list"));
            assertTrue(blockedList.isDisplayed());
            assertFalse(blockedItems.isEmpty());
        } else {
            assertTrue(true); // nema blokiranih, test prolazi
        }
    }

    @Test
    void shouldUnblockUser(){
        driver.get("http://localhost:3000/settings");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        List<WebElement> unblockButtons = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.className("ig-settings-unblock-btn")
                )
        );
        int initialCount = unblockButtons.size();
        unblockButtons.get(0).click();

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        longWait.until(driver ->
                driver.findElements(By.className("ig-settings-blocked-item")).size() < initialCount
        );

        List<WebElement> updatedItems = driver.findElements(By.className("ig-settings-blocked-item"));
        assertTrue(updatedItems.size() < initialCount);
    }

    @Test
    void shouldSaveShanges(){
        driver.get("http://localhost:3000/settings");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement submitButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.className("ig-settings-submit")
                )
        );

        // sa?ekaj da se forma u?ita pre klika
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        submitButton.click();

        WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement savedMessage = quickWait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-settings-saved")
                )
        );
        assertTrue(savedMessage.isDisplayed());
        assertTrue(savedMessage.getText().contains("Changes saved"));
    }
}
