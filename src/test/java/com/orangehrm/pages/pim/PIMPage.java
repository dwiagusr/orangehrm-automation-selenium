package com.orangehrm.pages.pim;

import io.qameta.allure.Step;
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

    private final By loadingSpinner = By.cssSelector(".oxd-loading-spinner");

    public PIMPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Step("Navigate to PIM module from Sidebar")
    public void navigateToPIM() {
        logger.info("Verifying Dashboard navigation before accessing PIM...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardContainer));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));

        try {
            WebElement pimMenuLink = wait.until(ExpectedConditions.visibilityOfElementLocated(pimMenu));
            pimMenuLink.click();
        } catch (Exception e) {
            logger.warn("Standard click failed, using JavaScript click fallback.");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(pimMenu));
        }

        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        wait.until(ExpectedConditions.urlContains("/pim/viewEmployeeList"));
    }

    @Step("Click on 'Add' Employee button")
    public void clickAddButton() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(addButton));
        try {
            addBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);
        }
    }

    @Step("Fill basic employee details: First Name: {0}, Middle Name: {1}, Last Name: {2}")
    public void fillEmployeeDetails(String firstName, String middleName, String lastName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
        driver.findElement(middleNameField).sendKeys(middleName);
        driver.findElement(lastNameField).sendKeys(lastName);

        WebElement idInput = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeIdField));
        String randomId = String.valueOf(1000 + new Random().nextInt(9000));

        idInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        idInput.sendKeys(randomId);

        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
    }

    @Step("Select dropdown option: {1}")
    private void selectDropdownOption(By dropdownLocator, String optionText) {
        wait.until(ExpectedConditions.elementToBeClickable(dropdownLocator)).click();
        String dropdownOptionWrapper = "//div[@role='listbox']//span[text()='%s']";
        By option = By.xpath(String.format(dropdownOptionWrapper, optionText));
        wait.until(ExpectedConditions.visibilityOfElementLocated(option)).click();
    }

    @Step("Fill personal details: Nationality: {0}, Marital Status: {1}, Gender: {2}")
    public void fillPersonalDetails(String nationality, String maritalStatus, String gender) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        selectDropdownOption(nationalityDropdown, nationality);
        selectDropdownOption(maritalStatusDropdown, maritalStatus);

        if (gender.equalsIgnoreCase("Male")) {
            driver.findElement(maleRadio).click();
        } else {
            driver.findElement(femaleRadio).click();
        }

        wait.until(ExpectedConditions.elementToBeClickable(savePersonalDetailsBtn)).click();
    }

    @Step("Verify if employee creation was successful and redirected to Personal Details")
    public boolean isSuccessSaved() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}