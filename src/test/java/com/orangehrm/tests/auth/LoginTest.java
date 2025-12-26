package com.orangehrm.tests.auth;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.auth.LoginPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Duration;

public class LoginTest extends BaseTest {

    @Test
    public void testValidLogin() {
        // Initialize Page Object
        LoginPage loginPage = new LoginPage(driver);

        // Perform login action
        loginPage.loginToApplication("Admin", "admin123");

        // Initialize Explicit Wait for synchronization
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait until URL contains "dashboard" before asserting
        // This is crucial for CI stability as page redirection takes time
        boolean isDashboard = wait.until(ExpectedConditions.urlContains("dashboard"));

        // Validate that login was successful
        Assert.assertTrue(isDashboard, "Login failed, the URL did not redirect to dashboard!");
    }
}