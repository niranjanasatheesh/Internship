package attrqa.framework.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * utility class to cater to file related stuff
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class FileHelper {

  public static String getTestExecutionPropertyValue(String sPropertyFileName, String sKey)
      throws IOException {
    String sValue;
    Properties properties = new Properties();
    File propertyFile = new File(
        System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
            + File.separator + "resources" + File.separator + sPropertyFileName);
    try (InputStream inputStream = new FileInputStream(propertyFile)) {
      properties.load(inputStream);
      sValue = properties.getProperty(sKey);
    }
    return sValue;
  }

}
