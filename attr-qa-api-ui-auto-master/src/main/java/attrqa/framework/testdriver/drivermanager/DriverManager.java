package attrqa.framework.testdriver.drivermanager;

import org.openqa.selenium.WebDriver;

/**
 * driver manager
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public abstract class DriverManager {
    protected WebDriver driver;

    protected abstract void createDriver();

    public void quitDriver() {
        if (null != driver) {
            driver.quit();
            driver = null;
        }

    }

    public WebDriver getDriver() {
        if (null == driver) {
            createDriver();
        }
        return driver;
    }
}
