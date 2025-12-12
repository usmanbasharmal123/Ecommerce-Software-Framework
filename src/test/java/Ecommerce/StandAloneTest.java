package Ecommerce;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import Ecommerce.TestComponents.BaseTest;
import Ecommerce.pageobjects.CartSectionPage;
import Ecommerce.pageobjects.CheckoutPage;
import Ecommerce.pageobjects.ConfirmationPage;
import Ecommerce.pageobjects.LoginPage;
import Ecommerce.pageobjects.ProductCatalogue;

public class StandAloneTest extends BaseTest {
@Test
    public  void submitOrderTest() throws IOException {
    	

        // Test data
        String userEmail = "usman.basharmal123@gmail.com";
        String userPassword = "R@hulshetty.123";
        String productName = "ZARA COAT 3";
        String countryName = "Pakistan";
        
     // Step 1: Launching Application            
        ProductCatalogue productCatalogue = loginPage.login(userEmail, userPassword);

        // Step 2: Add product to cart
        productCatalogue.addProductToCart(productName);

        // Step 3: Navigate to cart and verify product
        CartSectionPage cartPage = productCatalogue.goToCartPage();
        boolean isProductAvailable = cartPage.verifyProductDisplay(productName);
        Assert.assertTrue(isProductAvailable, "Product was not found in the cart!");

        // Step 4: Checkout process
        CheckoutPage checkoutPage = cartPage.checkout();
        checkoutPage.selectCountry(countryName);
        ConfirmationPage confirmationPage = checkoutPage.submitOrder();

        // Step 5: Validate order confirmation
        Assert.assertTrue(confirmationPage.getConfirmation(), "Order confirmation failed!");

    }

    
}
