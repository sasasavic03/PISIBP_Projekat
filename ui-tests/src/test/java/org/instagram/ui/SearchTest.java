package org.instagram.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchTest extends BaseTest {

    private void openSearchPanel() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(., 'Search')]")));

        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", searchButton);

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.className("ig-searchpanel")));
    }

    @Test
    void shouldShowSearchPanel() {
        driver.get("http://localhost:3000/feed");
        openSearchPanel();

        WebElement searchPanel = driver.findElement(By.className("ig-searchpanel"));
        assertTrue(searchPanel.isDisplayed());
    }

    @Test
    void shouldShowHintWhenQueryIsEmpty() {
        driver.get("http://localhost:3000/feed");
        openSearchPanel();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement hint = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-searchpanel-hint")));

        assertTrue(hint.getText().contains("Try searching for people."));
    }

    @Test
    void shouldShowResultsWhenQueryMatches() throws Exception {
        driver.get("http://localhost:3000/feed");
        openSearchPanel();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@placeholder='Search']")));

        // input ima autoFocus pa je vec fokusiran
        Thread.sleep(500);

        java.awt.Robot robot = new java.awt.Robot();
        robot.keyPress(java.awt.event.KeyEvent.VK_A);
        robot.keyRelease(java.awt.event.KeyEvent.VK_A);

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        longWait.until(ExpectedConditions.presenceOfElementLocated(
                By.className("ig-searchpanel-item")));

        List<WebElement> results = driver.findElements(By.className("ig-searchpanel-item"));
        assertFalse(results.isEmpty());
    }

    @Test
    void shouldShowNoResultsMessage() throws Exception {
        driver.get("http://localhost:3000/feed");
        openSearchPanel();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@placeholder='Search']")));

        Thread.sleep(500);
        java.awt.Robot robot = new java.awt.Robot();
        // kucaj "xyz"
        for (int keyCode : new int[]{
                java.awt.event.KeyEvent.VK_X,
                java.awt.event.KeyEvent.VK_Y,
                java.awt.event.KeyEvent.VK_Z}) {
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
        }

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement hint = longWait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-searchpanel-hint")));

        assertTrue(hint.getText().contains("No results for"));
    }

    @Test
    void shouldClearSearchInput() throws Exception {
        driver.get("http://localhost:3000/feed");
        openSearchPanel();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement input = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//input[@placeholder='Search']")));

        Thread.sleep(500);
        java.awt.Robot robot = new java.awt.Robot();
        robot.keyPress(java.awt.event.KeyEvent.VK_T);
        robot.keyRelease(java.awt.event.KeyEvent.VK_T);

        WebElement clearButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.className("ig-searchpanel-clear")));

        clearButton.click();
        assertTrue(input.getAttribute("value").isEmpty());
    }
}