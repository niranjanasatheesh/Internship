package attrqa.framework.testdriver.drivermanager;

import attrqa.framework.reporting.ExtentTestManager;
import java.io.File;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * chrome driver manager
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class ChromeDriverManager extends DriverManager {

    String sHeadless ;

    @Override
    public void createDriver() {
        sHeadless = System.getProperty("headless",  "false");
        String sChromeDriverFile;
        String sOperatingSystem = System.getProperty("os.name");
        ChromeOptions chromeOptions = new ChromeOptions();
        if(sOperatingSystem.contains("Windows")){
            sChromeDriverFile = "chromedriver.exe";
            chromeOptions.addArguments("--allow-running-insecure-content");
            chromeOptions.addArguments("--ignore-certificate-errors");
        }
        else{
            sChromeDriverFile = "chromedriver";
            chromeOptions.addArguments("--single-process");
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--user-data-dir=/tmp/user-data");
            chromeOptions.addArguments("--data-path=/tmp/data-path");
            chromeOptions.addArguments("--homedir=/tmp");
            chromeOptions.addArguments("--disk-cache-dir=/tmp/cache-dir");
        }
        String sChromeDriverPath = new File(System.getProperty("user.dir"), sChromeDriverFile).getAbsolutePath();
        ExtentTestManager.getExtentTest().info("setting 'webdriver.chrome.driver' to "+sChromeDriverPath);
        System.setProperty("webdriver.chrome.driver", sChromeDriverPath);

        if(null!=sHeadless){
            if(sHeadless.equalsIgnoreCase("true")){
                chromeOptions.addArguments("--headless");
            }
        }
        chromeOptions.addArguments("--incognito");

        driver = new ChromeDriver(chromeOptions);
    }

}
