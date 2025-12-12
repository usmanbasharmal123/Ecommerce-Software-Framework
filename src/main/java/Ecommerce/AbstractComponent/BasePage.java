package Ecommerce.AbstractComponent;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ecommerce.pageobjects.CartSectionPage;

/**
 * BasePage provides reusable Selenium actions with robust waits and logging.
 */
public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(BasePage.class);

    // Sensitive keywords loaded from GlobalData.properties
    private static List<String> sensitiveKeywords;

    static {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir")
                    + "//src//main//resources//GlobalData.properties");
            props.load(fis);
            String sensitiveFields = props.getProperty("sensitiveFields", "password,card,cvv,ssn");
            sensitiveKeywords = Arrays.asList(sensitiveFields.toLowerCase().split(","));
        } catch (IOException e) {
            logger.warn("Could not load sensitiveFields from GlobalData.properties. Using defaults.");
            sensitiveKeywords = Arrays.asList("password", "card", "cvv", "ssn");
        }
    }

    // Common locators
    private final By btnCart = By.cssSelector("button[routerlink*='cart']");
    private final By spinnerLocator = By.cssSelector("[class*='ngx-spinner-overlay']");

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ===== Wait Utilities =====
    public void waitForElementToAppear(By locator) {
        logger.debug("Waiting for element to appear: {}", locator.toString());
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        logger.debug("Element appeared: {}", locator.toString());
    }

    public void waitForElementToBeClickable(By locator) {
        logger.debug("Waiting for element to be clickable: {}", locator.toString());
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        logger.debug("Element is clickable: {}", locator.toString());
    }

    public void waitForElementToDisappear(By locator) {
        logger.debug("Waiting for element to disappear: {}", locator.toString());
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        logger.debug("Element disappeared: {}", locator.toString());
    }

    // ===== Common Actions =====
    protected void click(By locator) {
        logger.info("Clicking element: {}", locator.toString());
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        logger.info("Clicked element: {}", locator.toString());
    }

    protected void type(By locator, String text) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(text);

        String locatorString = locator.toString().toLowerCase();
        boolean isSensitive = sensitiveKeywords.stream().anyMatch(locatorString::contains);

        if (isSensitive) {
            logger.info("Typed '[PROTECTED]' into element: {}", locator.toString());
        } else {
            logger.info("Typed '{}' into element: {}", text, locator.toString());
        }
    }

    protected String getText(By locator) {
        logger.info("Retrieving text from element: {}", locator.toString());
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
        logger.info("Text retrieved: '{}'", text);
        return text;
    }

    /**
     * Safe click method that waits for spinner overlay to disappear
     * and retries with JavaScript click if intercepted.
     */
    public void safeClick(By locator) {
        try {
            logger.info("Attempting safe click on element: {}", locator.toString());
            waitForElementToDisappear(spinnerLocator);
            click(locator);
            logger.info("Safe click succeeded on element: {}", locator.toString());
        } catch (ElementClickInterceptedException e) {
            logger.warn("Click intercepted by overlay for '{}'. Retrying with JS click.", locator.toString());
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(locator));
            logger.info("JS click succeeded on element: {}", locator.toString());
        }
    }

    // ===== Navigation =====
    public CartSectionPage goToCartPage() {
        logger.info("Navigating to Cart page...");
        safeClick(btnCart);
        logger.info("Navigation to Cart page successful.");
        return new CartSectionPage(driver);
    }
}
