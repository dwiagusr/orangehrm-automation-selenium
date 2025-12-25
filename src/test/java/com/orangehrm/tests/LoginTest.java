package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void testValidLogin() {
        // Init Page Object
        LoginPage loginPage = new LoginPage(driver);

        // Action
        loginPage.loginToApplication("Admin", "admin123");

        // Assertion (Validasi URL berubah)
        String actualUrl = driver.getCurrentUrl();
        Assert.assertTrue(actualUrl.contains("dashboard"), "Gagal login, URL tidak mengarah ke dashboard!");
    }
}