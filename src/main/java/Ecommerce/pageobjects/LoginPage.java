package Ecommerce.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ecommerce.AbstractComponent.BasePage;

/**
 * LoginPage Page Object
 * ---------------------
 * Represents the login page and provides methods
 * to perform login actions, capture error messages,
 * and navigate to the login page.
 *
 * This class follows the Page Object Model (POM) design pattern.
 */
public class LoginPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    // ===== Locators =====
    // Input fields
    private final By usernameField = By.id("userEmail");       // Email input field
    private final By passwordField = By.id("userPassword");    // Password input field
    private final By btnLogin = By.id("login");                // Login button

    // Error message containers
    private final By flyinOut = By.cssSelector("[class*='flyInOut']"); // Legacy error popup (e.g. incorrect credentials)
    private final By textboxErrorLocator = By.cssSelector("div.invalid-feedback div"); // Inline textbox error (e.g. invalid email format)

    // ===== Constructor =====
    public LoginPage(WebDriver driver) {
        super(driver); // Pass WebDriver to BasePage for common actions
    }

    // ===== Actions =====

    /**
     * Logs in using provided credentials.
     * Uses reusable methods from BasePage (type, click),
     * which already include explicit waits.
     *
     * @param userEmail    User email address
     * @param userPassword User password
     * @return ProductCatalogue instance after successful login
     */
    public ProductCatalogue login(String userEmail, String userPassword) {
        logger.info("Attempting login with user '{}'", userEmail);

        // Step 1: Enter email
        type(usernameField, userEmail);
        logger.debug("Entered username: {}", userEmail);

        // Step 2: Enter password
        type(passwordField, userPassword);
        logger.debug("Entered password for user '{}'", userEmail);

        // Step 3: Click login button
        click(btnLogin);
        logger.info("Login button clicked for user '{}'", userEmail);

        // Step 4: Return ProductCatalogue page object (next page after login)
        return new ProductCatalogue(driver);
    }

    // ===== Error Message Handling =====

    /**
     * Captures error message from legacy flyInOut popup.
     * Typically used for invalid credentials (wrong email/password).
     *
     * @return Error message text
     */
    public String getFlyinOutErrorMessage() {
        waitForElementToAppear(flyinOut); // Wait until popup is visible
        return driver.findElement(flyinOut).getText(); // Return popup text
    }

    /**
     * Captures inline textbox error message.
     * Typically used for invalid email format or field validation errors.
     *
     * @return Error message text
     */
    public String getTextboxErrorMessage() {
        waitForElementToAppear(textboxErrorLocator); // Wait until inline error is visible
        return driver.findElement(textboxErrorLocator).getText(); // Return inline error text
    }

    // ===== Navigation =====

    /**
     * Navigates directly to the login page.
     */
    public void goTo() {
        logger.info("Navigating to login page...");
        driver.get("https://rahulshettyacademy.com/client");
        logger.info("Login page loaded successfully.");
    }
}
