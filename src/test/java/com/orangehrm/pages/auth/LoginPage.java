package com.orangehrm.pages.auth;

import io.qameta.allure.Step; // Pastikan import ini ada
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private final WebDriver driver;

    // 1. Locators
    private final By usernameField = By.name("username");
    private final By passwordField = By.name("password");
    private final By loginButton = By.xpath("//button[@type='submit']");

    // 2. Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // 3. Actions with Allure Steps
    @Step("Enter username: {0}")
    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    @Step("Enter password: {0}")
    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    @Step("Click on Login button")
    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    /**
     * Helper method to perform a complete login flow.
     * {0} refers to user, {1} refers to pass
     */
    @Step("Login to OrangeHRM with username: {0}")
    public void loginToApplication(String user, String pass) {
        enterUsername(user);
        enterPassword(pass);
        clickLogin();
    }
}