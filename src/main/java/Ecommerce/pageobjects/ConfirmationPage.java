package Ecommerce.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ecommerce.AbstractComponent.BasePage;

/**
 * ConfirmationPage Page Object
 * 
 * Represents the order confirmation page and provides methods
 * to validate the success message after checkout.
 */
public class ConfirmationPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmationPage.class);

    // ===== Locators =====
    private final By confirmation = By.className("hero-primary");

    // ===== Constructor =====
    public ConfirmationPage(WebDriver driver) {
        super(driver);
    }

    // ===== Actions =====

    /**
     * Validates the confirmation message displayed after placing an order.
     *
     * @return true if the confirmation message matches expected text, false otherwise
     */
    public Boolean getConfirmation() {
        logger.info("Waiting for confirmation message to appear...");
        waitForElementToAppear(confirmation);

        String confirmationText = driver.findElement(confirmation).getText();
        logger.info("Confirmation message retrieved: '{}'", confirmationText);

        boolean isConfirmed = confirmationText.equalsIgnoreCase("Thankyou for the order.");

        if (isConfirmed) {
            logger.info("Order confirmation validated successfully.");
        } else {
            logger.warn("Order confirmation did not match expected text. Actual: '{}'", confirmationText);
        }

        return isConfirmed;
    }
}
