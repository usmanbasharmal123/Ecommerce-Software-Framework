package Ecommerce;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import Ecommerce.TestComponents.BaseTest;
import Ecommerce.pageobjects.CartSectionPage;
import Ecommerce.pageobjects.ProductCatalogue;

/**
 * ProductErrorValidationTest
 * --------------------------
 * Purpose: Verify that the cart does not falsely show a product
 *          that was not added.
 *
 * This test class focuses ONLY on product/cart-related negative scenarios.
 */
public class ProductErrorValidationTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ProductCatalogue.class);

    @Test
    public void productErrorValidationTest() throws IOException {
        // Test data: valid credentials and product details
        String userEmail = "usman.basharmal123@gmail.com";
        String userPassword = "R@hulshetty.123";
        String productName = "ZARA COAT 3";   // actual product to add

        logger.info("Starting product error validation test with user: {}", userEmail);

        // Step 1: Login with valid credentials and navigate to product catalogue
        ProductCatalogue productCatalogue = loginPage.login(userEmail, userPassword);

        // Step 2: Add a valid product to cart
        productCatalogue.addProductToCart(productName);
        logger.info("Added product '{}' to cart.", productName);

        // Step 3: Navigate to cart page and verify product presence
        CartSectionPage cartPage = productCatalogue.goToCartPage();

        // Intentionally checking for a wrong product name to validate negative scenario
        boolean isProductAvailable = cartPage.verifyProductDisplay("ZARA COAT 33");
        logger.info("Verification result for product 'ZARA COAT 33': {}", isProductAvailable);

        // Assertion: Product should NOT be found in cart
        Assert.assertFalse(isProductAvailable, "Unexpected product was found in the cart!");
    }
}
