package com.orangehrm.tests.pim;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.auth.LoginPage;
import com.orangehrm.pages.pim.PIMPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Duration;

public class AddEmployeeTest extends BaseTest {

    @Test
    public void testAddNewEmployee() {
        // Step 1: Initialize LoginPage and Login to application
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToApplication("Admin", "admin123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("dashboard"));

        // Step 2: Initialize PIMPage and navigate to PIM module
        PIMPage pimPage = new PIMPage(driver);
        pimPage.navigateToPIM();

        // Step 3: Add a new employee
        pimPage.clickAddButton();
        pimPage.fillEmployeeDetails("Automated", "Employee", "12345");

        // Step 4: Assertion - Verify that the employee was saved successfully
        boolean isSaved = pimPage.isSuccessSaved();
        Assert.assertTrue(isSaved, "The employee was not saved or the Personal Details page did not load!");
    }
}