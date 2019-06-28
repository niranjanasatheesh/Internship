package attrqa.framework.pageobjects.maestro;

import attrqa.framework.pageobjects.base.BasePage;
import attrqa.framework.reporting.ExtentTestManager;
import attrqa.framework.testdriver.driver.Driver;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;

/**
 * marketing intelligence admin page object
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class MarketingIntelligenceAdminPage extends BasePage {

  FluentWait wait;

  public MarketingIntelligenceAdminPage(Driver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
    wait = new FluentWait(driver.getDriver())
        .withTimeout(20, TimeUnit.SECONDS)
        .pollingEvery(1, TimeUnit.SECONDS)
        .ignoring(Exception.class);
    waitForLoad();
  }

  @FindBy(xpath="//span[text()='Dashboard']")
  private WebElement dashboardMenu;

  @FindBy(xpath="//span[text()='Configuration']")
  private WebElement configurationMenu;

  @FindBy(xpath="//span[text()='Refresh']")
  private WebElement refreshMenu;

  @FindBy(xpath="//span[text()='Data Model']")
  private WebElement dataModelMenu;


  @Override
  protected void waitForLoad() {
    WebElement dashboard;
    dashboard = (WebElement) wait.until(new Function<WebDriver, WebElement>() {
      public WebElement apply(WebDriver driver) {
        return driver.findElement(By.xpath("//div[contains(text(),'Hello from dashboard !!!')]"));
      }
    });
  }

  public void expandConfigurationMenu(){
    configurationMenu.click();
    ExtentTestManager.getExtentTest().info("expanding Configuration Menu");
  }

  public void clickRefreshMenu(){
    refreshMenu.click();
    ExtentTestManager.getExtentTest().info("clicking Refresh Menu");
    waitForRefreshConfigurationScreenToLoad();
  }

  protected void waitForRefreshConfigurationScreenToLoad(){
    ExtentTestManager.getExtentTest().info("waiting for refresh configuration screen to load");
    WebElement cardTitle;
    cardTitle = (WebElement) wait.until(new Function<WebDriver, WebElement>() {
      public WebElement apply(WebDriver driver) {
        return driver.findElement(By.xpath("//div[text()='Configuration :: Refresh']"));
      }
    });
  }

  //vrt

}
