package attrqa.framework.helper;


import java.io.IOException;

/**
 * utility class to cater to properties file related stuff
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class PropertiesHelper {

  public static String getHostStringFromPropertiesFile(String sPropertyFileName, Class clazz)
      throws IOException {
    String sClassName = clazz.getSimpleName();
    String sHost = FileHelper
        .getTestExecutionPropertyValue(sPropertyFileName, "host_" + sClassName);
    return sHost;
  }

  public static String getJSONTestDataFileNameFromPropertiesFile(String sPropertyFileName,
      String sClassName) throws IOException {
    String sTestDataFileName = FileHelper
        .getTestExecutionPropertyValue(sPropertyFileName, "json_" + sClassName);
    return sTestDataFileName;
  }

  public static String getTestDataFileNameFromPropertiesFile(String sPropertyFileName,
      String sClassName) throws IOException {
    String sTestDataFileName = FileHelper
        .getTestExecutionPropertyValue(sPropertyFileName, "workbook_" + sClassName);
    return sTestDataFileName;
  }

  public static String getTestDataSheetNameFromPropertiesFile(String sPropertyFileName,
      String sClassName) throws IOException {
    String sTestDataSheetName = FileHelper
        .getTestExecutionPropertyValue(sPropertyFileName, "sheet_" + sClassName);
    return sTestDataSheetName;
  }

  public static String getLoginURLForAuthorizationTokenFromPropertiesFile(String sPropertyFileName, Class clazz)
          throws IOException {
    String sClassName = clazz.getSimpleName();
    String sURL = FileHelper
            .getTestExecutionPropertyValue(sPropertyFileName, "authorization-url_" + sClassName);
    return sURL;
  }
  public static String getClientIdFromPropertiesFile(String sPropertyFileName, Class clazz)
      throws IOException {
    String sClassName = clazz.getSimpleName();
    String sTestDataSheetName = FileHelper
        .getTestExecutionPropertyValue(sPropertyFileName, "clientId_" + sClassName);
    return sTestDataSheetName;
  }


  public static String getValuePropertiesFile(String sPropertyFileName, String sKey)
      throws IOException {
    String sValue = FileHelper.getTestExecutionPropertyValue(sPropertyFileName, sKey);
    return sValue;
  }

  public static String getUserNameFromPropertiesFile(String sPropertyFileName, Class clazz)
          throws IOException {
    String sClassName = clazz.getSimpleName();
    String sUserName = FileHelper
            .getTestExecutionPropertyValue(sPropertyFileName, "username_" + sClassName);
    return sUserName;
  }

  public static String getPasswordFromPropertiesFile(String sPropertyFileName, Class clazz)
          throws IOException {
    String sClassName = clazz.getSimpleName();
    String sPassword = FileHelper
            .getTestExecutionPropertyValue(sPropertyFileName, "password_" + sClassName);
    return sPassword;
  }


}
