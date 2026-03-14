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

public class FeedTest extends BaseTest{

    @Test
    void shouldShowFeedPage(){
        driver.get("http://localhost:5173/feed");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement postList = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("post-list")));

        assertTrue(postList.isDisplayed());
    }

    @Test
    void shouldShowPosts(){
        driver.get("http://localhost:5173/feed");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.className("post-list")));

        List<WebElement> posts = driver.findElements(By.className("ig-post_card"));
        assertFalse(posts.isEmpty());
    }

    @Test
    void shouldShowSuggestions(){
        driver.get("http://localhost:5173/feed");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement suggestions = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-suggestions")));

        assertTrue(suggestions.isDisplayed());
    }

    @Test
    void shouldShowSuggestionsHeader(){
        driver.get("http://localhost:5173/feed");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement header = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-suggestions_header")));

        assertTrue(header.isDisplayed());
        assertTrue(header.getText().contains("Suggestions for you"));
    }

    @Test
    void shouldNavigatetoProfileFromSuggestion(){
        driver.get("http://localhost:5173/feed");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement suggestionLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-suggestions_link")
                )
        );

        suggestionLink.click();

        wait.until(ExpectedConditions.urlContains("/profile/"));
        assertTrue(driver.getCurrentUrl().contains("/profile/"));
    }
}
