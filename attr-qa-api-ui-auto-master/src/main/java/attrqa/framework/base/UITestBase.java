package attrqa.framework.base;

import attrqa.framework.pageobjects.factory.PageInstanceFactory;
import attrqa.framework.reporting.ExtentReportManager;
import attrqa.framework.reporting.ExtentTestManager;
import attrqa.framework.testdriver.driver.Driver;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import java.io.IOException;
import java.lang.reflect.Method;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * The parent class of all UI test scripts
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class UITestBase {

  private static ExtentReports extentReports;
  private ExtentTest extentTest;

  protected static String sPropertyFileName;

  public Driver driver;
  public static ThreadLocal<Driver> driverThreadLocal = new ThreadLocal<Driver>();

  public PageInstanceFactory pageInstanceFactory;


  @BeforeSuite
  @Parameters({"properties"})  //reads parameter value for name="properties" from testng.xml
  public void beforeSuite(@Optional("nielsen") String sProperties, ITestContext testContext) {
    String suiteName = testContext.getSuite().getName();
    extentReports = ExtentReportManager.createReportInstance(suiteName);
    // read -Dproperties value from mvn cmd if not null, else default to testng parameter
    sPropertyFileName = System.getProperty("properties", sProperties + ".properties");
    //set the propertiesFileName to testContext to be retrieved in subclasses and data-provider
    //set the propertiesFileName to ISuite so that data can be shared between two tests
    testContext.getSuite().setAttribute("propertyFileName", sPropertyFileName);

  }

  @BeforeMethod
  public void beforeMethod(Method method) {
    Test test = method.getDeclaredAnnotation(Test.class);
    extentTest = extentReports.createTest(method.getName(), test.description());
    ExtentTestManager.setExtentTest(extentTest);
    driver = new Driver();
    driverThreadLocal.set(driver);
  }

  /**
   * The annotated method will be run after each test method. This does the reporting (extent
   * reports) for the status of the test case
   */
  @AfterMethod(alwaysRun = true)
  public void afterMethod(ITestResult iTestResult) {
    ExtentTest extentTest = ExtentTestManager.getExtentTest();
    if (iTestResult.getStatus() == ITestResult.SUCCESS) {
      extentTest.log(Status.PASS, iTestResult.getMethod().getMethodName() + " is a pass");
    } else if (iTestResult.getStatus() == ITestResult.FAILURE) {
      String screenshotFile = driverThreadLocal.get().takeScreenShot(iTestResult.getMethod().getMethodName());
      try {
        extentTest
            .addScreenCaptureFromPath(screenshotFile, iTestResult.getMethod().getMethodName());
      }
      catch (IOException ioe){
        extentTest.info("unable to attach screenshot");
      }
      extentTest.log(Status.FAIL, iTestResult.getThrowable().getMessage());
    } else if (iTestResult.getStatus() == ITestResult.SKIP) {
      extentTest.log(Status.SKIP, iTestResult.getThrowable().getMessage());
    }
    driverThreadLocal.get().quit();
  }


  @AfterSuite
  public void afterSuite(ITestContext testContext) {
    extentReports.flush();
  }

  public void logInfo(String sInfo) {
    ExtentTest extentTest = ExtentTestManager.getExtentTest();
    extentTest.log(Status.INFO, sInfo);
  }

  public void setSuiteContextValue(String sKey, String sValue, ITestContext testContext) {
    testContext.getSuite().setAttribute(sKey, sValue);
    ExtentTest extentTest = ExtentTestManager.getExtentTest();
    extentTest.log(Status.INFO,
        "setting " + sKey + " as attribute with value " + sValue + " in suite-context");
  }

  public void setSuiteContextValue(String sKey, Object oValue, ITestContext testContext) {
    testContext.getSuite().setAttribute(sKey, oValue);
    ExtentTest extentTest = ExtentTestManager.getExtentTest();
    extentTest.log(Status.INFO,
        "setting " + sKey + " as attribute with value " + oValue.toString() + " in suite-context");
  }

  public String getSuiteContextValue(String sKey, ITestContext testContext) {
    ExtentTest extentTest = ExtentTestManager.getExtentTest();
    String sValue = "";
    Object storedObject = testContext.getSuite().getAttribute(sKey);
    if (storedObject != null) {
      sValue = testContext.getSuite().getAttribute(sKey).toString();
      extentTest.log(Status.INFO,
          "retrieving " + sValue + " as value against attribute " + sKey + " from suite-context");
    } else {
      extentTest.fail("test-context value not found for key " + sKey);
    }
    return sValue;
  }

}
