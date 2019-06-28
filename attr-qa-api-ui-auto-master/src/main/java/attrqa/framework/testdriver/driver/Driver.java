package attrqa.framework.testdriver.driver;

import attrqa.framework.reporting.ExtentTestManager;
import attrqa.framework.testdriver.drivermanager.DriverManager;
import attrqa.framework.testdriver.factory.DriverManagerFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * wrapper on web driver
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class Driver implements WebDriver {

  private String sBrowser;
  private WebDriver driver;

  public Driver(){
    //get browser from mvn cmd or default to CHROME
    sBrowser = System.getProperty("browser",  "CHROME");
    DriverManager driverManager = DriverManagerFactory.getDriverManager(sBrowser);
    driver = driverManager.getDriver();
    driver.manage().window().maximize();
  }

  public WebDriver getDriver(){
    return driver;
  }

  @Override
  public void get(String url) {
    driver.get(url);
    ExtentTestManager.getExtentTest().info("loading url: "+url);
  }

  @Override
  public String getCurrentUrl() {
    ExtentTestManager.getExtentTest().info("getting current url as: "+driver.getCurrentUrl());
    return driver.getCurrentUrl();
  }

  @Override
  public String getTitle() {
    ExtentTestManager.getExtentTest().info("getting page Title as: "+driver.getTitle());
    return driver.getTitle();
  }

  @Override
  public List<WebElement> findElements(By by) {
    return driver.findElements(by);
  }

  @Override
  public WebElement findElement(By by) {
    return driver.findElement(by);
  }

  @Override
  public String getPageSource() {
    return driver.getPageSource();
  }

  @Override
  public void close() {
    driver.close();
  }

  @Override
  public void quit() {
    driver.quit();
    ExtentTestManager.getExtentTest().info("quitting webdriver session");
  }

  @Override
  public Set<String> getWindowHandles() {
    return driver.getWindowHandles();
  }

  @Override
  public String getWindowHandle() {
    return driver.getWindowHandle();
  }

  @Override
  public TargetLocator switchTo() {
    return driver.switchTo();
  }

  @Override
  public Navigation navigate() {
    return driver.navigate();
  }

  @Override
  public Options manage() {
    return driver.manage();
  }

  public String takeScreenShot(String testName) {
    testName = testName.replaceAll("\\.", "_");
    String filePath = System.getProperty("user.dir")+ File.separator +"reports"+File.separator +"screenshots"+ File.separator;
    String fileName = filePath+System.currentTimeMillis()+"_"+testName+".png";
    File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
    try {
      FileUtils.copyFile(screenshotFile, new File(fileName));
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    ExtentTestManager.getExtentTest().info("captured screenshot "+fileName);
    return fileName;
  }

}
