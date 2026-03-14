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

public class ProfileTest extends BaseTest{

    @Test
    void shouldShowProfileHeader(){
        driver.get("http://localhost:5173/profile/john.doe");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement header = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-profile-header")
                )
        );

        assertTrue(header.isDisplayed());
    }

    @Test
    void shouldShowProfileStats(){
        driver.get("http://localhost:5173/profile/john.doe");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement stats = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-profile-stats")
                )
        );

        assertTrue(stats.isDisplayed());
        assertTrue(stats.getText().contains("posts"));
        assertTrue(stats.getText().contains("followers"));
        assertTrue(stats.getText().contains("following"));
    }

    @Test
    void shouldShowPostsGrid(){
        driver.get("http://localhost:5173/profile/john.doe");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement grid = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-posts-grid")
                )
        );

        assertTrue(grid.isDisplayed());

        List<WebElement> posts = driver.findElements(By.className("ig-post-item"));
        assertFalse(posts.isEmpty());
    }

    @Test
    void shouldShowPrivateProfileMessage(){
        driver.get("http://localhost:5173/profile/sarah.j");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement privateView = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-private-view")
                )
        );

        assertTrue(privateView.isDisplayed());
        assertTrue(privateView.getText().contains("This account is private"));
    }

    @Test
    void shouldShowBlockedView(){
        driver.get("http://localhost:5173/profile/jane.smith");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement blockedView = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-blocked-view")
                )
        );

        assertTrue(blockedView.isDisplayed());
        assertTrue(blockedView.getText().contains("You have blocked this account"));
    }

    @Test
    void shouldUnblockUser(){
        driver.get("http://localhost:5173/profile/jane.smith");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement unblockButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("ig-blocked-unblock-btn")
                )
        );

        unblockButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.className("ig-profile-header")
        ));

        assertTrue(driver.findElement(By.className("ig-profile-header")).isDisplayed());
    }
}
