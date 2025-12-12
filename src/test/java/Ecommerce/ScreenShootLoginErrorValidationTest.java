package Ecommerce;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sun.net.httpserver.Authenticator.Retry;

import Ecommerce.TestComponents.BaseTest;
import Ecommerce.TestComponents.RetryToRunTheFailedTest;
import Ecommerce.pageobjects.LoginPage;

/**
 * LoginErrorValidationTest
 * ------------------------
 * Purpose: Verify that the application shows the correct error message
 *          when invalid credentials are used.
 *
 * This test class focuses ONLY on login-related negative scenarios.
 */
public class ScreenShootLoginErrorValidationTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    /**
     * DataProvider supplies multiple sets of test data:
     * - Invalid credentials
     * - Empty credentials
     * - Invalid email format
     *
     * Each row contains:
     *   [0] -> Email
     *   [1] -> Password
     *   [2] -> Expected error message
     *   [3] -> Error type ("flyinOut" or "textbox")
     * @throws IOException 
     */
    @DataProvider(name = "loginErrorData")
    public Object[][] getLoginErrorData() throws IOException {
    	List<HashMap<String,String>> data=getDataFromJson();
        return new Object[][] {
           
            { data.get(2) }
        };
    }

    /**
     * Test method that runs for each dataset provided by DataProvider.
     */
    @Test(dataProvider = "loginErrorData",retryAnalyzer=RetryToRunTheFailedTest.class)
    public void loginErrorValidationTest(HashMap<String,String> input) throws IOException {
        logger.info("Starting login error validation test with email: '{}'",input.get("userEmail"));

        // Step 1: Attempt login with provided credentials
        loginPage.login(input.get("userEmail"), input.get("userPassword"));

        // Step 2: Capture error message based on error type
        String actualError;
        if ("flyinOut".equals(input.get("errorType"))) {
            actualError = loginPage.getFlyinOutErrorMessage();
        } else {
            actualError = loginPage.getTextboxErrorMessage();
        }

        logger.info("Captured error message: '{}'", actualError);

        // Step 3: Assertion
   
        logger.info("We forcelly fail the funtion to take the screenshot : '{}'",actualError);
        Assert.assertEquals(actualError, "We forsely fail the funtion to take the screenshot");
    }
}
