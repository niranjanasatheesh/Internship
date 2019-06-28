package attrqa.framework.pageobjects.maestro;

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
 * refresh configuration page element
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class RefreshConfigurationPageElement extends BasePage {

  FluentWait wait;

  public RefreshConfigurationPageElement(Driver driver){
    super(driver);
    PageFactory.initElements(driver, this);
    wait = new FluentWait(driver.getDriver())
        .withTimeout(20, TimeUnit.SECONDS)
        .pollingEvery(1, TimeUnit.SECONDS)
        .ignoring(Exception.class);
    waitForLoad();
  }

  @FindBy(xpath = "//mat-select[@placeholder='Refresh Cadence']")
  private WebElement refreshCadenceSelect;

  @FindBy(xpath="//span[@class='mat-option-text' and normalize-space(text())='Daily']")
  private WebElement refreshCadenceDailyOptionText;

  @FindBy(xpath="//span[@class='mat-option-text' and normalize-space(text())='Weekly']")
  private WebElement refreshCadenceWeeklyOptionText;

  @FindBy(xpath="//span[@class='mat-option-text' and normalize-space(text())='Monthly']")
  private WebElement refreshCadenceMonthlyOptionText;



  @Override
  protected void waitForLoad(){
    WebElement cardTitle;
    cardTitle = (WebElement) wait.until(new Function<WebDriver, WebElement>() {
      public WebElement apply(WebDriver driver) {
        return driver.findElement(By.xpath("//div[text()='Configuration :: Refresh']"));
      }
    });
  }


  public void setRefreshCadence(String sOption){
    refreshCadenceSelect.findElement(By.xpath("//div[@class='mat-select-arrow']")).click();
    if(sOption.equalsIgnoreCase("daily")){
      refreshCadenceDailyOptionText.click();
    }else if(sOption.equalsIgnoreCase("monthly")){
      refreshCadenceMonthlyOptionText.click();
    }else if(sOption.equalsIgnoreCase("weekly")){
      refreshCadenceWeeklyOptionText.click();
    }
  }




}
