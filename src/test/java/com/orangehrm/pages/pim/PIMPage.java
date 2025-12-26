package com.orangehrm.pages.pim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Random;
import java.time.Duration;

public class PIMPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(PIMPage.class);

    // --- Locators ---
    private final By pimMenu = By.xpath("//span[text()='PIM']");
    private final By addButton = By.xpath("//button[normalize-space()='Add']");
    private final By firstNameField = By.xpath("//input[@placeholder='First Name']");
    private final By middleNameField = By.xpath("//input[@placeholder='Middle Name']");
    private final By lastNameField = By.xpath("//input[@placeholder='Last Name']");
    // Robust Locator: Finding Input based on the "Employee Id" label
    private final By employeeIdField = By.xpath("//label[text()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private final By saveButton = By.xpath("//button[@type='submit']");
    private final By personalDetailsHeader = By.xpath("//h6[normalize-space()='Personal Details']");

    public PIMPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Navigates to the PIM menu from the dashboard.
     * Uses JS Click as a fallback for headless mode stability.
     */
    public void navigateToPIM() {
        logger.info("Navigating to PIM menu...");
        WebElement pimMenuLink = wait.until(ExpectedConditions.presenceOfElementLocated(pimMenu));
        try {
            pimMenuLink.click();
        } catch (Exception e) {
            logger.warn("Standard click failed. Using JavaScript Click for PIM Menu.");
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", pimMenuLink);
        }
    }

    public void clickAddButton() {
        logger.info("Clicking the 'Add Employee' button.");
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
    }

    /**
     * Fills employee details including a randomly generated Employee ID.
     */
    public void fillEmployeeDetails(String firstName, String middleName, String lastName) {
        logger.info("Entering details for: {} {} {}", firstName, middleName, lastName);

        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
        driver.findElement(middleNameField).sendKeys(middleName);
        driver.findElement(lastNameField).sendKeys(lastName);

        // Handle Employee ID: Clear existing and input random
        WebElement idInput = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeIdField));
        String randomId = String.valueOf(1000 + new Random().nextInt(9000));

        // Simulating human 'Select All' then 'Backspace' to ensure the field is empty
        idInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        idInput.sendKeys(randomId);
        logger.info("Employee ID successfully set to: {}", randomId);

        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
        logger.info("Save button clicked.");
    }

    /**
     * Checks if the Personal Details page is displayed after saving.
     * @return true if successful, false otherwise.
     */
    public boolean isSuccessSaved() {
        try {
            boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader)).isDisplayed();
            logger.info("Successfully redirected to Personal Details page.");
            return isDisplayed;
        } catch (Exception e) {
            logger.error("Verification failed: 'Personal Details' header not found within timeout.");
            return false;
        }
    }
}