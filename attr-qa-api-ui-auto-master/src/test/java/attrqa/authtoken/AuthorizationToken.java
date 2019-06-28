package attrqa.authtoken;

import attrqa.framework.base.APITestBase;
import attrqa.framework.helper.PropertiesHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;

public class AuthorizationToken extends APITestBase {

    String sURL;
    String sUserName;
    String sPassword;
    String sParamAuthorization;
    String sAuthorizationToken;

    @BeforeClass
    public void getEnvironment(ITestContext testContext) throws IOException {
        sURL = PropertiesHelper
                .getLoginURLForAuthorizationTokenFromPropertiesFile(sPropertyFileName, AuthorizationToken.class);
        sUserName = PropertiesHelper
                .getUserNameFromPropertiesFile(sPropertyFileName, AuthorizationToken.class);
        sPassword = PropertiesHelper
                .getPasswordFromPropertiesFile(sPropertyFileName, AuthorizationToken.class);

    }


    @Parameters({"authToken"})
    @BeforeClass
    public void getEnvironment(@Optional("") String authToken) {
        sParamAuthorization = authToken;

    }


    @Test
    public void doTest(ITestContext testContext) throws Exception {

        if (!sParamAuthorization.equalsIgnoreCase("")) {
            sAuthorizationToken = sParamAuthorization;
            //System.out.println("Reading token from testng xml file (parameter value) ");
            logInfo("Reading AuthToken from TestNG XML File (parameter value)");
        } else {
            String firefoxDriverpath = "";

            String os = System.getProperty("os.name");
            System.out.println(os + " " + System.getProperty("user.dir"));

            if (!os.contains("indows")) {
                firefoxDriverpath =
                        System.getProperty("user.dir") + File.separator
                                + "geckodriver";
            } else {
                firefoxDriverpath =
                        System.getProperty("user.dir") + File.separator
                                + "geckodriver.exe";


            }

            System.setProperty("webdriver.gecko.driver", firefoxDriverpath);

            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");

            WebDriver driver = new FirefoxDriver(options);


            driver.get(sURL);

            By byNielsenPanel = By.id("loginForm");
            WebElement nielsenPanel = (new WebDriverWait(driver, 50000))
                    .until(ExpectedConditions.presenceOfElementLocated(byNielsenPanel));
            driver.findElement(By.id("USER")).sendKeys(sUserName);
            driver.findElement(By.id("PASSWORD")).sendKeys(sPassword);
            driver.findElement(By.id("btnLogin")).click();

            // By byHeader = By.xpath("//span[@class='app-title'][contains(text(),'Attribution Optimization Tool')]");
            By byHeader = By.xpath("//div[@id='main-route-container']");
            WebElement chartHeader = (new WebDriverWait(driver, 2000))
                    .until(ExpectedConditions.presenceOfElementLocated(byHeader));


            Thread.sleep(5000);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            sAuthorizationToken = (js.executeScript("return sessionStorage.getItem('mroiAuthorizationToken');")).toString();
            driver.quit();

        }

        setSuiteContextValue("authorizationtoken", sAuthorizationToken, testContext);
    }
}
