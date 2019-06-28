package attrqa.framework.testdriver.factory;

import attrqa.framework.testdriver.drivermanager.ChromeDriverManager;
import attrqa.framework.testdriver.drivermanager.DriverManager;
import attrqa.framework.testdriver.drivermanager.FirefoxDriverManager;

/**
 * factory to generate driver manager instances
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class DriverManagerFactory {

  public static DriverManager getDriverManager(String sBrowser) {
    DriverManager driverManager = null;

    if (sBrowser.equalsIgnoreCase("CHROME")) {
      driverManager = new ChromeDriverManager();
    } else if (sBrowser.equalsIgnoreCase("FIREFOX")) {
      driverManager = new FirefoxDriverManager();
    }

    return driverManager;
  }

}
