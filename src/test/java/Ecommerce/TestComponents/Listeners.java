package Ecommerce.TestComponents;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import resources.ExtentReportNG;

/**
 * Listeners class
 * ----------------
 * Implements TestNG ITestListener to integrate ExtentReports.
 * Captures screenshots on failure and logs test status.
 */
public class Listeners extends BaseTest implements ITestListener {

    // Logger instance
    private static final Logger logger = LoggerFactory.getLogger(Listeners.class);

    // ExtentReports instance
    ExtentReports extent = ExtentReportNG.getExtendReportObject();

    // Thread-safe ExtentTest (one per test thread)
    ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test); // assign unique test instance per thread
        logger.info("Test started: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "Test Passed");
        logger.info("Test passed: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().fail(result.getThrowable());
        logger.error("Test failed: {} with exception {}", 
                     result.getMethod().getMethodName(), 
                     result.getThrowable().getMessage());

        WebDriver driver = null;
        try {
            driver = (WebDriver) result.getTestClass()
                                       .getRealClass()
                                       .getField("driver")
                                       .get(result.getInstance());
            logger.debug("WebDriver instance retrieved for screenshot.");
        } catch (Exception e) {
            logger.error("Could not access WebDriver for screenshot: {}", e.getMessage());
        }

        if (driver != null) {
            try {
                String path = getScreenshoot(result.getMethod().getMethodName(), driver);
                extentTest.get().addScreenCaptureFromPath(path, result.getMethod().getMethodName());
                logger.info("Screenshot captured and attached: {}", path);
            } catch (IOException e) {
                logger.error("Screenshot capture failed: {}", e.getMessage());
            }
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        logger.info("All tests finished. Extent report flushed.");
    }
}
