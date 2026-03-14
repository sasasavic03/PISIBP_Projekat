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

public class SearchTest extends BaseTest {

    private void openSearchPanel() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[.//span[text()='Search']]")));
        searchButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.className("ig-searchpanel")));
    }

    @Test
    void shouldShowSearchPanel() {
        driver.get("http://localhost:5173/feed");
        openSearchPanel();


        WebElement searchPanel = driver.findElement(By.className("ig-searchpanel"));
        assertTrue(searchPanel.isDisplayed());
    }

    @Test
    void shouldShowHintWhenQueryIsEmpty() {
        driver.get("http://localhost:5173/feed");
        openSearchPanel();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement hint = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-searchpanel-hint")));

        assertTrue(hint.getText().contains("Try searching for people."));
    }

    @Test
    void shouldShowResultsWhenQueryMatches() {
        driver.get("http://localhost:5173/feed");
        openSearchPanel();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement input = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//input[@placeholder='Search']")));

        input.sendKeys("a");

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.className("ig-searchpanel-item")));

        List<WebElement> results = driver.findElements(By.className("ig-searchpanel-item"));
        assertFalse(results.isEmpty());
    }

    @Test
    void shouldShowNoResultsMessage() {
        driver.get("http://localhost:5173/feed");
        openSearchPanel();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement input = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//input[@placeholder='Search']")));

        input.sendKeys("xyzxyzxyz123");

        WebElement hint = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-searchpanel-hint")));

        assertTrue(hint.getText().contains("No results for"));
    }

    @Test
    void shouldClearSearchInput() {
        driver.get("http://localhost:5173/feed");
        openSearchPanel();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement input = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//input[@placeholder='Search']")));

        input.sendKeys("test");

        WebElement clearButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.className("ig-searchpanel-clear")));

        clearButton.click();

        assertTrue(input.getAttribute("value").isEmpty());
    }
}