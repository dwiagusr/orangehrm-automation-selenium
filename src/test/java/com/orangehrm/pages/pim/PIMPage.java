package com.orangehrm.pages.pim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.JavascriptExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Random;
import java.time.Duration;

public class PIMPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(PIMPage.class);

    // --- Add Employee Locators ---
    private final By pimMenu = By.xpath("//span[text()='PIM']/parent::a");
    private final By dashboardContainer = By.className("oxd-layout-navigation");
    // Enhanced locator for the 'Add' button to be more specific
    private final By addButton = By.xpath("//div[@class='orangehrm-header-container']//button[normalize-space()='Add']");
    private final By firstNameField = By.xpath("//input[@placeholder='First Name']");
    private final By middleNameField = By.xpath("//input[@placeholder='Middle Name']");
    private final By lastNameField = By.xpath("//input[@placeholder='Last Name']");
    private final By employeeIdField = By.xpath("//label[text()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private final By saveButton = By.xpath("//button[@type='submit']");

    // --- Personal Details Locators ---
    private final By personalDetailsHeader = By.xpath("//h6[normalize-space()='Personal Details']");
    private final By nationalityDropdown = By.xpath("//label[text()='Nationality']/ancestor::div[contains(@class,'oxd-input-group')]//div[@class='oxd-select-text-input']");
    private final By maritalStatusDropdown = By.xpath("//label[text()='Marital Status']/ancestor::div[contains(@class,'oxd-input-group')]//div[@class='oxd-select-text-input']");
    private final By maleRadio = By.xpath("//label[text()='Male']/span");
    private final By femaleRadio = By.xpath("//label[text()='Female']/span");
    private final By savePersonalDetailsBtn = By.xpath("//label[text()='Nationality']/ancestor::form//button[@type='submit']");

    // Global loading spinner for OrangeHRM SPA synchronization
    private final By loadingSpinner = By.cssSelector(".oxd-loading-spinner");

    public PIMPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Navigates to the PIM menu from the dashboard.
     * Implements explicit wait for the spinner to ensure page stability.
     */
    public void navigateToPIM() {
        logger.info("Verifying Dashboard navigation before accessing PIM...");
        // 1. Ensure the navigation sidebar is present in the DOM
        wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardContainer));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        logger.info("Attempting to click PIM menu...");

        try {
            WebElement pimMenuLink = wait.until(ExpectedConditions.visibilityOfElementLocated(pimMenu));
            pimMenuLink.click();
        } catch (Exception e) {
            logger.warn("Standard click failed or intercepted, using JavaScript click fallback.");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(pimMenu));
        }
        // 3. Synchronization after navigation
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        wait.until(ExpectedConditions.urlContains("/pim/viewEmployeeList"));
        logger.info("PIM Page fully loaded.");
    }

    /**
     * Clicks the 'Add' button to open the Add Employee form.
     * Uses a robust click strategy to handle potential headless mode issues.
     */
    public void clickAddButton() {
        logger.info("Waiting for 'Add' button to be clickable...");
        // Wait for potential background loading to finish
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));

        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(addButton));
        try {
            addBtn.click();
        } catch (Exception e) {
            logger.warn("Standard click intercepted; attempting JavaScript click as fallback.");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);
        }
        logger.info("Add button successfully clicked.");
    }

    /**
     * Fills employee details with automatic uniqueness for Employee ID.
     */
    public void fillEmployeeDetails(String firstName, String middleName, String lastName) {
        logger.info("Filling employee fields for: {} {} {}", firstName, middleName, lastName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
        driver.findElement(middleNameField).sendKeys(middleName);
        driver.findElement(lastNameField).sendKeys(lastName);

        // Generate and set a random 4-digit Employee ID
        WebElement idInput = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeIdField));
        String randomId = String.valueOf(1000 + new Random().nextInt(9000));

        // Clear existing value using keyboard shortcuts for consistency
        idInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        idInput.sendKeys(randomId);
        logger.info("Random Employee ID set: {}", randomId);

        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
        logger.info("Save button clicked, awaiting redirection.");
    }

    /**
     * Helper method to interact with custom OrangeHRM dropdowns.
     */
    private void selectDropdownOption(By dropdownLocator, String optionText) {
        wait.until(ExpectedConditions.elementToBeClickable(dropdownLocator)).click();

        // Dynamic XPath for generic option selection within the listbox
        String dropdownOptionWrapper = "//div[@role='listbox']//span[text()='%s']";
        By option = By.xpath(String.format(dropdownOptionWrapper, optionText));
        wait.until(ExpectedConditions.visibilityOfElementLocated(option)).click();
    }

    /**
     * Completes the Personal Details form after a successful employee creation.
     */
    public void fillPersonalDetails(String nationality, String maritalStatus, String gender) {
        logger.info("Completing Personal Details form...");

        // Sync before starting inputs
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));

        selectDropdownOption(nationalityDropdown, nationality);
        selectDropdownOption(maritalStatusDropdown, maritalStatus);

        if (gender.equalsIgnoreCase("Male")) {
            driver.findElement(maleRadio).click();
        } else {
            driver.findElement(femaleRadio).click();
        }

        wait.until(ExpectedConditions.elementToBeClickable(savePersonalDetailsBtn)).click();
        logger.info("Personal Details submitted.");
    }

    /**
     * Validates if the application redirected the user to the profile page.
     * @return true if Personal Details header is visible.
     */
    public boolean isSuccessSaved() {
        try {
            boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader)).isDisplayed();
            logger.info("Confirmed redirection to the Personal Details page.");
            return isDisplayed;
        } catch (Exception e) {
            logger.error("Redirection validation failed: Header not found within timeout.");
            return false;
        }
    }
}