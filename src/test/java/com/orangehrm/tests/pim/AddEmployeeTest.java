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
    public void testAddNewEmployeeAndFillPersonalDetails() {
        // Step 1: Initialize LoginPage and Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToApplication("Admin", "admin123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("dashboard"));

        // Step 2: Navigate to PIM module
        PIMPage pimPage = new PIMPage(driver);
        pimPage.navigateToPIM();

        // Step 3: Add a new employee (First Step)
        pimPage.clickAddButton();
        // Now using random ID inside this method as we updated before
        pimPage.fillEmployeeDetails("Automated", "Selenium", "Java");

        // Step 4: Verify redirection to Personal Details page
        boolean isRedirected = pimPage.isSuccessSaved();
        Assert.assertTrue(isRedirected, "Failed to redirect to Personal Details page after saving new employee!");

        // Step 5: Fill additional Personal Details (Second Step)
        // Data: Nationality, Marital Status, Gender
        pimPage.fillPersonalDetails("Indonesian", "Single", "Male");

        // Step 6: Final Assertion (Optional: Verify Success Message/Toast if needed)
        // For now, if no error occurs during fillPersonalDetails, we consider it a success
        System.out.println("End-to-End Add Employee and Personal Details fill successful.");
    }
}