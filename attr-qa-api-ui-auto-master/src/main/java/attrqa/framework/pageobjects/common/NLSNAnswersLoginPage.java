package attrqa.framework.pageobjects.common;

import attrqa.framework.pageobjects.base.BasePage;
import attrqa.framework.reporting.ExtentTestManager;
import attrqa.framework.testdriver.driver.Driver;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * nielsen answers login page object
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class NLSNAnswersLoginPage extends BasePage {

  public NLSNAnswersLoginPage(Driver driver){
    super(driver);
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath="//h1")
  private WebElement headerText;

  @FindBy(xpath="//input[@id='USER']")
  private WebElement usernameInputText;

  @FindBy(xpath="//input[@id='PASSWORD']")
  private WebElement passwordInputText;

  @FindBy(id="rememberMe")
  private WebElement rememberMeCheckbox;

  @FindBy(id="btnLogin")
  private WebElement loginButton;

  @FindBy(xpath="//a[text()='Forgot Password / New to Nielsen']")
  private WebElement forgotPassword_NewToNielsen;

  @FindBy(xpath="//a[text()='Need Support Information']")
  private WebElement needSupportInformation;

  public void launch(String url){
    driver.get(url);
    waitForLoad();
  }

  @Override
  protected void waitForLoad() {
    ExtentTestManager.getExtentTest().info("waiting for redirect to Nielsen Answers page");
    FluentWait<WebDriver> fluentWait = new FluentWait<>(driver.getDriver())
        .withTimeout(Duration.ofSeconds(25))
        .pollingEvery(Duration.ofSeconds(1))
        .ignoring(NoSuchElementException.class);

    WebElement authenticatingMessage;
    authenticatingMessage = fluentWait.until(new Function<WebDriver, WebElement>() {
      @Override
      public WebElement apply(WebDriver webDriver) {
        return driver.findElement(By.xpath("//span[@class='alert-message' and text()='Authenticating...']"));
      }
    });

    if(authenticatingMessage!=null){
      System.out.println("waiting for redirect @ "+System.currentTimeMillis());
      WebDriverWait webDriverWait = new WebDriverWait(driver,25);
      webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//span[@class='alert-message' and text()='Authenticating...']")));
      System.out.println("authenticating disappeared @ "+System.currentTimeMillis());
    }

    try {
      URL url = new URL(driver.getCurrentUrl());
      System.out.println(driver.getCurrentUrl());
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      boolean redirect = false;
      int status = connection.getResponseCode();
      System.out.println("response code "+status);
      if (status != HttpURLConnection.HTTP_OK) {
        if (status == HttpURLConnection.HTTP_MOVED_TEMP
            || status == HttpURLConnection.HTTP_MOVED_PERM
            || status == HttpURLConnection.HTTP_SEE_OTHER)
          redirect = true;
      }
      if(redirect){
        System.out.println("Redirecting...");
      }
    }
    catch (Exception e){
      e.printStackTrace();
    }

    WebElement nielsenAnswersLoginPageHeader;
    nielsenAnswersLoginPageHeader = fluentWait.until(new Function<WebDriver, WebElement>() {
      public WebElement apply(WebDriver driver) {
        return driver.findElement(By.cssSelector(".nielsen-page-header"));
      }
    });
    WebDriverWait wait = new WebDriverWait(driver, 25);
    wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.cssSelector(".nielsen-page-header")),"WELCOME TO NIELSEN ANSWERS"));
    ExtentTestManager.getExtentTest().info("redirected to Nielsen Answers page");
  }

  public void doLogin(String sUsername, String sPassword, boolean rememberMe){
    if(!rememberMe){
      usernameInputText.clear();
      usernameInputText.sendKeys(sUsername);
      passwordInputText.clear();
      passwordInputText.sendKeys(sPassword);
      loginButton.click();
    } else{
      usernameInputText.clear();
      usernameInputText.sendKeys(sUsername);
      passwordInputText.clear();
      passwordInputText.sendKeys(sPassword);
      rememberMeCheckbox.click();
      loginButton.click();
    }
    ExtentTestManager.getExtentTest().info("performing NLSN Answers login with "+sUsername);
  }

  public String getPageHeader(){
    return headerText.getText();
  }

}
