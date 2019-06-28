package attrqa.framework.reporting;

import com.aventstack.extentreports.ExtentTest;
import java.util.HashMap;
import java.util.Map;


/**
 * holds the mapping information of current test thread to
 * relevant extent report instance
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class ExtentTestManager {

  private static Map<Long, ExtentTest> extentTestMap = new HashMap<>();

  public static synchronized ExtentTest getExtentTest() {
    return extentTestMap.get((Thread.currentThread().getId()));
  }

  public static synchronized void setExtentTest(ExtentTest test) {
    extentTestMap.put((Thread.currentThread().getId()), test);
  }


}
