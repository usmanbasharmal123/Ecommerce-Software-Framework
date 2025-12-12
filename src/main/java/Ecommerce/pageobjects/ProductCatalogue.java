package Ecommerce.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ecommerce.AbstractComponent.BasePage;

/**
 * ProductCatalogue Page Object
 */
public class ProductCatalogue extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(ProductCatalogue.class);

    // Locators
    private final By products = By.cssSelector(".mb-3");
    private final By productNameLocator = By.xpath(".//b");
    private final By addToCartButton = By.xpath(".//button[text()=' Add To Cart']");
    private final By toastNotification = By.cssSelector("#toast-container");
    private final By ngAnimation = By.cssSelector("[class*='ng-animating']");

    public ProductCatalogue(WebDriver driver) {
        super(driver);
    }

    public List<WebElement> getProductList() {
        logger.info("Fetching product list from catalogue page...");
        waitForElementToAppear(products);
        List<WebElement> productList = driver.findElements(products);
        logger.debug("Found {} products", productList.size());
        return productList;
    }

    public void addProductToCart(String productName) {
        logger.info("Attempting to add product '{}' to cart", productName);

        WebElement product = getProductList()
                .stream()
                .filter(p -> p.findElement(productNameLocator)
                        .getText()
                        .equalsIgnoreCase(productName))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Product '{}' not found in catalogue", productName);
                    return new RuntimeException("Product not found: " + productName);
                });

        product.findElement(addToCartButton).click();
        logger.info("Clicked 'Add To Cart' for product '{}'", productName);

        waitForElementToAppear(toastNotification);
        waitForElementToDisappear(ngAnimation);

        logger.info("Product '{}' successfully added to cart", productName);
    }
}
