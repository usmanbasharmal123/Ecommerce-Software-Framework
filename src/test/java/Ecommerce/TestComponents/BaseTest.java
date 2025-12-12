package Ecommerce.TestComponents; 
// Package declaration: groups related classes together under Ecommerce.TestComponents

import java.io.File;                // For file handling
import java.io.FileInputStream;     // For reading files as input streams
import java.io.IOException;         // For handling IO exceptions
import java.util.HashMap;           // For storing key-value pairs
import java.util.List;              // For working with lists
import java.util.Properties;        // For reading .properties configuration files

import org.apache.commons.io.FileUtils; // Utility class for file operations (copy, read, write)
import org.openqa.selenium.OutputType;  // Defines screenshot output type
import org.openqa.selenium.TakesScreenshot; // Interface for capturing screenshots
import org.openqa.selenium.WebDriver;   // Main Selenium WebDriver interface
import org.openqa.selenium.chrome.ChromeDriver;   // Chrome browser driver
import org.openqa.selenium.edge.EdgeDriver;       // Edge browser driver
import org.openqa.selenium.firefox.FirefoxDriver; // Firefox browser driver
import org.testng.annotations.AfterMethod;        // TestNG annotation: runs after each test method
import org.testng.annotations.BeforeMethod;       // TestNG annotation: runs before each test method

import com.fasterxml.jackson.core.type.TypeReference; // Helps parse JSON into generic types
import com.fasterxml.jackson.databind.ObjectMapper;   // JSON parser/mapper

import Ecommerce.pageobjects.LoginPage; // Import your LoginPage page object
import io.github.bonigarcia.wdm.WebDriverManager; // Manages browser driver binaries automatically

import org.slf4j.Logger;        // Logging interface
import org.slf4j.LoggerFactory; // Factory to create logger instances

/**
 * BaseTest class
 * ----------------
 * Provides common setup and teardown logic for all test classes.
 * Responsibilities:
 *   - Read configuration (browser type) from GlobalData.properties
 *   - Initialize the appropriate WebDriver
 *   - Launch the application and return LoginPage object
 *   - Close the browser after each test
 */
public class BaseTest {

    // Logger instance for structured logging
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    // WebDriver instance shared across tests
    public WebDriver driver;

    // Page object for login functionality
    public LoginPage loginPage;

    /**
     * Initializes WebDriver based on browser specified in GlobalData.properties.
     * Supported browsers: Chrome, Firefox, Edge.
     *
     * @return WebDriver instance
     * @throws IOException if properties file cannot be read
     */
    public WebDriver initializeDriver() throws IOException {
        Properties pro = new Properties(); // Create Properties object to load config

        // Load configuration file containing browser name
        FileInputStream fil = new FileInputStream(System.getProperty("user.dir")
                + "//src//main//resources//GlobalData.properties");
        pro.load(fil); // Load properties file into memory

        // Check if browser is passed via system property, else use properties file
        String browserName = System.getProperty("browser") != null
                ? System.getProperty("browser")
                : pro.getProperty("browser");

        logger.info("Initializing WebDriver for browser: {}", browserName);

        // Launch browser based on configuration
        if (browserName.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup(); // Auto-download ChromeDriver
            driver = new ChromeDriver();             // Launch Chrome
            logger.debug("ChromeDriver initialized successfully.");
        } else if (browserName.equalsIgnoreCase("fireFox")) {
            WebDriverManager.firefoxdriver().setup(); // Auto-download FirefoxDriver
            driver = new FirefoxDriver();             // Launch Firefox
            logger.debug("FirefoxDriver initialized successfully.");
        } else if (browserName.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup(); // Auto-download EdgeDriver
            driver = new EdgeDriver();             // Launch Edge
            logger.debug("EdgeDriver initialized successfully.");
        } else {
            logger.error("Unsupported browser specified: {}", browserName);
            throw new RuntimeException("Unsupported browser: " + browserName);
        }

        // Maximize browser window
        driver.manage().window().maximize();
        logger.info("Browser window maximized.");

        return driver; // Return driver instance
    }

    /**
     * Utility method to read test data from JSON file.
     *
     * @return List of HashMaps containing test data
     * @throws IOException if file cannot be read
     */
    public List<HashMap<String, String>> getDataFromJson() throws IOException {
        // Define path to JSON test data file
        String path = System.getProperty("user.dir") + "//src//main//resources//JSonData";
        logger.info("Loading test data from JSON file: {}", path);

        // Read JSON file into string
        String jsonContent = FileUtils.readFileToString(new File(path), "UTF-8");

        // Parse JSON into List<HashMap<String,String>>
        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String, String>> data = mapper.readValue(
                jsonContent,
                new TypeReference<List<HashMap<String, String>>>() {}
        );

        logger.debug("Test data loaded successfully. Records count: {}", data.size());
        return data; // Return parsed test data
    }

    /**
     * Captures screenshot and saves it under reports/screenshots folder.
     *
     * @param testCaseFileName name of the test case
     * @param driver WebDriver instance
     * @return relative path for embedding in HTML report
     * @throws IOException if screenshot capture fails
     */
    public static String getScreenshoot(String testCaseFileName, WebDriver driver) throws IOException {
        // Define screenshot directory
        String screenshotDir = System.getProperty("user.dir") + "//reports//screenshots//";
        File screenshotFolder = new File(screenshotDir);

        // Create folder if it doesn't exist
        if (!screenshotFolder.exists()) {
            screenshotFolder.mkdirs();
        }

        // Capture screenshot
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Define destination path
        String screenshotPath = screenshotDir + testCaseFileName + ".png";
        File destination = new File(screenshotPath);

        // Copy screenshot file to destination
        FileUtils.copyFile(src, destination);

        // Log screenshot path
        LoggerFactory.getLogger(BaseTest.class).info("Screenshot saved at: {}", screenshotPath);

        // Return relative path for embedding in ExtentReports
        return "screenshots/" + testCaseFileName + ".png";
    }

    /**
     * Runs before each test method.
     * Initializes the driver, launches the application, and returns LoginPage.
     *
     * @return LoginPage object for further interactions
     * @throws IOException if driver initialization fails
     */
    @BeforeMethod()
    public LoginPage launchApplication() throws IOException {
        driver = initializeDriver(); // Initialize WebDriver

        // Create LoginPage object and navigate to application URL
        loginPage = new LoginPage(driver);
        loginPage.goTo();
        logger.info("Application launched and navigated to LoginPage.");

        return loginPage; // Return LoginPage object
    }

    /**
     * Runs after each test method.
     * Ensures the browser is closed to avoid resource leaks.
     */
    @AfterMethod()
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Close browser
            logger.info("Browser closed successfully after test execution.");
        }
    }
}
