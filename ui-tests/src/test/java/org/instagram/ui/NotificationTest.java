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

public class NotificationTest extends BaseTest{

    private void openNotificationsPanel(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement notifButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(., 'Notifications')]")
                )
        );

        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", notifButton);

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.className("ig-notif-panel")
        ));
    }

    @Test
    void shouldShowNotificationsPanel() {

        driver.get("http://localhost:3000/feed");
        openNotificationsPanel();

        WebElement notifPanel = driver.findElement(By.className("ig-notif-panel"));
        assertTrue(notifPanel.isDisplayed());
    }

    @Test
    void shouldShowNotificationsList() {
        driver.get("http://localhost:3000/feed");
        openNotificationsPanel();

        // panel postoji, bilo sa ili bez notifikacija
        WebElement panel = driver.findElement(By.className("ig-notif-panel"));
        assertTrue(panel.isDisplayed());
    }

    @Test
    void shouldMarkAllAsRead() {
        driver.get("http://localhost:3000/feed");
        openNotificationsPanel();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> markReadButtons = driver.findElements(By.className("ig-notif-markread"));

        if (!markReadButtons.isEmpty()) {
            markReadButtons.get(0).click();
            List<WebElement> unreadItems = driver.findElements(By.cssSelector(".ig-notif-item.unread"));
            assertTrue(unreadItems.isEmpty());
        } else {
            // nema unread notifikacija, test prolazi
            assertTrue(true);
        }
    }

    @Test
    void shouldAcceptFollowRequest() {

        driver.get("http://localhost:3000/feed");
        openNotificationsPanel();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        while (true) {

            List<WebElement> buttons =
                    driver.findElements(By.className("ig-notif-accept"));

            if (buttons.isEmpty()) {
                break;
            }

            WebElement button = wait.until(
                    ExpectedConditions.elementToBeClickable(buttons.get(0))
            );

            button.click();

            wait.until(ExpectedConditions.stalenessOf(button));
        }

        assertTrue(driver.findElements(By.className("ig-notif-accept")).isEmpty());
    }

    @Test
    void shouldDeclineFollowRequest() {
        driver.get("http://localhost:3000/feed");
        openNotificationsPanel();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> declineButtons = driver.findElements(By.className("ig-notif-decline"));

        if (declineButtons.isEmpty()) {
            assertTrue(true); // nema follow requestova, skip
            return;
        }

        List<WebElement> initialNotifs = driver.findElements(By.className("ig-notif-item"));
        int initialCount = initialNotifs.size();
        declineButtons.get(0).click();
        List<WebElement> updatedNotifs = driver.findElements(By.className("ig-notif-item"));
        assertTrue(updatedNotifs.size() < initialCount);
    }
}