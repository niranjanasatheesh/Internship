package attrqa.framework.testdriver.drivermanager;

import attrqa.framework.reporting.ExtentTestManager;
import java.io.File;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * firefox driver manager
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class FirefoxDriverManager extends DriverManager {


    @Override
    public void createDriver() {
        String sGeckoDriverFile;
        String sOperatingSystem = System.getProperty("os.name");
        if(sOperatingSystem.contains("Windows")){
            sGeckoDriverFile = "geckodriver.exe";
        }
        else{
            sGeckoDriverFile = "geckodriver";
        }
        String sGeckoDriverPath = new File(System.getProperty("user.dir"), sGeckoDriverFile).getAbsolutePath();
        ExtentTestManager.getExtentTest().info("setting 'webdriver.gecko.driver' to "+sGeckoDriverPath);
        System.setProperty("webdriver.gecko.driver", sGeckoDriverPath);
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--headless");
        driver = new FirefoxDriver(firefoxOptions);
    }
}
