package attrqa.framework.pageobjects.optimusprime;

import attrqa.framework.pageobjects.base.BasePage;
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
 * attr optimization tool page object
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class AttributionOptimizationToolPage extends BasePage {

  FluentWait wait;

  public AttributionOptimizationToolPage(Driver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
    wait = new FluentWait(driver.getDriver())
        .withTimeout(20, TimeUnit.SECONDS)
        .pollingEvery(1, TimeUnit.SECONDS)
        .ignoring(Exception.class);
    waitForLoad();
  }

  @FindBy(tagName = "google-chart")
  private WebElement googleChartTag;

  @FindBy(xpath = "//app-optimization/div/app-media-plan-summary/div/mat-card/div/div")
  private WebElement sectionHeaderLabelText;

  @Override
  protected void waitForLoad() {
    WebElement googleChart;
    googleChart = (WebElement) wait.until(new Function<WebDriver, WebElement>() {
      public WebElement apply(WebDriver driver) {
        return driver.findElement(By.tagName("google-chart"));
      }
    });
  }

  public String getSectionHeaderLabelText() {
    return sectionHeaderLabelText.getText();
  }

}
