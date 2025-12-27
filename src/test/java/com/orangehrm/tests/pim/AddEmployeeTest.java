package com.orangehrm.tests.pim;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.auth.LoginPage;
import com.orangehrm.pages.pim.PIMPage;
import com.orangehrm.utils.ExcelUtil;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.time.Duration;

public class AddEmployeeTest extends BaseTest {

    /**
     * DataProvider to fetch employee details from the Excel file.
     * The file must be located in src/test/resources/data/
     */
    @DataProvider(name = "employeeExcelData")
    public Object[][] getEmployeeData() {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/data/EmployeeData.xlsx";
        // "Sheet1" must match the name of the tab in your Excel file
        return ExcelUtil.getTestData(filePath, "Sheet1");
    }

    @Test(dataProvider = "employeeExcelData")
    public void testAddNewEmployeeDDT(String firstName, String middleName, String lastName,
                                      String nationality, String maritalStatus, String gender) {

        // Step 1: Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToApplication("Admin", "admin123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("dashboard"));

        // Step 2: Navigate to PIM
        PIMPage pimPage = new PIMPage(driver);
        pimPage.navigateToPIM();

        // Step 3: Add New Employee with data from Excel
        pimPage.clickAddButton();
        pimPage.fillEmployeeDetails(firstName, middleName, lastName);

        // Step 4: Validate redirection
        Assert.assertTrue(pimPage.isSuccessSaved(), "Failed to redirect to Personal Details page!");

        // Step 5: Fill Personal Details with data from Excel
        pimPage.fillPersonalDetails(nationality, maritalStatus, gender);

        System.out.println("End-to-End test completed for: " + firstName + " " + lastName);
    }
}