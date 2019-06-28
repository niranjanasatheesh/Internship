package attrqa.framework.pageobjects.base;

import attrqa.framework.pageobjects.factory.PageInstanceFactory;
import attrqa.framework.testdriver.driver.Driver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * parent class of all page-objects
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public abstract class BasePage extends PageInstanceFactory {

  public BasePage(Driver driver) {
    super(driver);
  }

  protected abstract void waitForLoad();

  public String getAuthorizationToken(){
    WebDriver webDriver = driver.getDriver();
    JavascriptExecutor js = (JavascriptExecutor)webDriver;
    String sAuthorizationToken = (js.executeScript("return sessionStorage.getItem('mroiAuthorizationToken');")).toString();
    return sAuthorizationToken;
  }

}
