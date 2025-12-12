package Ecommerce.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ecommerce.AbstractComponent.BasePage;

/**
 * CartSectionPage Page Object
 * 
 * Represents the cart section and provides methods
 * to verify products and proceed to checkout.
 */
public class CartSectionPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(CartSectionPage.class);

    // ===== Locators =====
    private final By productsAddedToCart = By.cssSelector(".cart h3");
    private final By ngAnimation = By.cssSelector("[class*='ng-animating']");
    private final By btnCheckout = By.xpath("//li[@class='totalRow']//button");

    // ===== Constructor =====
    public CartSectionPage(WebDriver driver) {
        super(driver);
    }

    // ===== Actions =====

    /**
     * Retrieves the list of products currently added to the cart.
     *
     * @return List of WebElements representing products in the cart
     */
    public List<WebElement> getProducts() {
        logger.info("Fetching products added to cart...");
        waitForElementToAppear(productsAddedToCart);

        List<WebElement> products = driver.findElements(productsAddedToCart);
        waitForElementToDisappear(ngAnimation);

        logger.info("Items added to cart successfully. Total items: {}", products.size());
        return products;
    }

    /**
     * Verifies if a specific product is displayed in the cart.
     *
     * @param productName Name of the product to verify
     * @return true if product is found, false otherwise
     */
    public boolean verifyProductDisplay(String productName) {
        logger.info("Verifying if product '{}' is displayed in the cart...", productName);

        boolean checkAvailability = getProducts()
                .stream()
                .anyMatch(product -> product.getText().equalsIgnoreCase(productName));

        if (checkAvailability) {
            logger.info("Product '{}' is present in the cart.", productName);
        } else {
            logger.warn("Product '{}' is NOT present in the cart.", productName);
        }

        return checkAvailability;
    }

    /**
     * Proceeds to checkout from the cart section.
     *
     * @return CheckoutPage instance
     */
    public CheckoutPage checkout() {
        logger.info("Proceeding to checkout...");
        safeClick(btnCheckout);
        logger.info("Checkout button clicked successfully.");
        return new CheckoutPage(driver);
    }

}
