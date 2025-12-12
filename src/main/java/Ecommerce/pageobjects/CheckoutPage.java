package Ecommerce.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ecommerce.AbstractComponent.BasePage;

/**
 * CheckoutPage Page Object
 * 
 * Represents the checkout page and provides methods
 * to select a country and submit the order.
 */
public class CheckoutPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutPage.class);

    // ===== Locators =====
    private final By selectCountryInput = By.cssSelector("input[placeholder='Select Country']");
    private final By resultsContainer = By.cssSelector(".ta-results");
    private final By countryItems = By.cssSelector(".ta-item");
    private final By btnSubmit = By.cssSelector("a[class*='action__submit']");

    // ===== Constructor =====
    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // ===== Actions =====

    /**
     * Selects a country from the dropdown list.
     *
     * @param countryName Name of the country to select
     */
    public void selectCountry(String countryName) {
        logger.info("Selecting country '{}'", countryName);

        type(selectCountryInput, countryName);
        waitForElementToAppear(resultsContainer);

        List<WebElement> results = driver.findElements(countryItems);
        logger.debug("Found {} country options in dropdown.", results.size());

        WebElement selectedCountry = results.stream()
                .filter(option -> option.getText().trim().equalsIgnoreCase(countryName))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Country '{}' not found in dropdown.", countryName);
                    return new RuntimeException("Country not found in dropdown: " + countryName);
                });

        selectedCountry.click();
        logger.info("Country '{}' selected successfully.", countryName);
    }

    /**
     * Submits the order and navigates to the confirmation page.
     *
     * @return ConfirmationPage instance
     */
    public ConfirmationPage submitOrder() {
        logger.info("Submitting order...");
        safeClick(btnSubmit);
        logger.info("Order submitted. Navigating to ConfirmationPage.");
        return new ConfirmationPage(driver);
    }
}
