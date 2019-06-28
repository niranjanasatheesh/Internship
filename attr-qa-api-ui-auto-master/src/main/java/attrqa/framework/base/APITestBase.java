package attrqa.framework.base;

import attrqa.framework.helper.PropertiesHelper;
import attrqa.framework.helper.ValidationsHelper;
import attrqa.framework.reporting.ExtentReportManager;
import attrqa.framework.reporting.ExtentTestManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import io.restassured.response.Response;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * The parent class of all API test scripts
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class APITestBase {

  private static ExtentReports extentReports;
  private ExtentTest extentTest;

  protected static String sPropertyFileName;
  protected static String sEnv;
  protected static String sLogToMongo;

  private static Set<String> methodNameSet = new HashSet<>();
  private static int iteration;

  private static String sRunId;
  private static long lRunInitiatedAt;


  @BeforeSuite
  @Parameters({"properties"})  //reads parameter value for name="properties" from testng.xml
  public void beforeSuite(@Optional("nielsen") String sProperties, ITestContext testContext) {
    String suiteName = testContext.getSuite().getName();
    extentReports = ExtentReportManager.createReportInstance(suiteName);
    // read -Dproperties value from mvn cmd if not null, else default to testng parameter
    sEnv =System.getProperty("env");
    sLogToMongo = System.getProperty("logToMongo");
    if(sLogToMongo.length()==0){
      sLogToMongo = "false";
    }
    sPropertyFileName = System.getProperty("properties", sProperties + ".properties");
    //set the propertiesFileName to testContext to be retrieved in subclasses and data-provider
    //set the propertiesFileName to ISuite so that data can be shared between two tests
    testContext.getSuite().setAttribute("propertyFileName", sPropertyFileName);

    //run id for central db
    sRunId = generateRandomString(8);
    lRunInitiatedAt = System.currentTimeMillis();
  }

  @BeforeMethod
  public void beforeMethod(Object[] data, Method method, ITestContext testContext) {
    String testCaseId;
    Test test = method.getDeclaredAnnotation(Test.class);
    if (methodNameSet.add(method.getName())) {
      iteration = 1;
    } else {
      iteration++;
    }

    if(method.getDeclaringClass().getSimpleName().startsWith("AuthorizationToken")){
      extentTest = extentReports.createTest("AuthorizationToken", "Settinng AuthorizationToken Value");
    }

      else if (!method.getDeclaringClass().getSimpleName().equalsIgnoreCase("SQLRunner")){
        Map<String, String> dataMap = (HashMap<String, String>) data[0];
        if (dataMap.get("testcaseid") != null && dataMap.get("testcaseid").length() != 0) {
          testCaseId = method.getName() + " - " + dataMap.get("testcaseid");
        } else {
          testCaseId = method.getName() + " " + iteration;
        }
        extentTest = extentReports.createTest(testCaseId, test.description());
        Markup markup = MarkupHelper.createLabel(data[0].toString(), ExtentColor.BLACK);
        extentTest.log(Status.INFO, markup);
      }

      else {
        extentTest = extentReports.createTest("data-set-up", "preparing data by running sqls");
      }

    ExtentTestManager.setExtentTest(extentTest);
  }

  /**
   * The annotated method will be run after each test method. This does the reporting (extent
   * reports) for the status of the test case
   */
  @AfterMethod(alwaysRun = true)
  public void afterMethod(ITestResult iTestResult) throws IOException {
    ExtentTest extentTest = ExtentTestManager.getExtentTest();
    if (iTestResult.getStatus() == ITestResult.SUCCESS) {
      extentTest.log(Status.PASS, iTestResult.getMethod().getMethodName() + " is a pass");
    } else if (iTestResult.getStatus() == ITestResult.FAILURE) {
      extentTest.log(Status.FAIL, iTestResult.getThrowable().getMessage());
    } else if (iTestResult.getStatus() == ITestResult.SKIP) {
      extentTest.log(Status.SKIP, iTestResult.getThrowable().getMessage());
    }
    if(sLogToMongo.equalsIgnoreCase("true")){
      String sHostName = InetAddress.getLocalHost().getHostName();
      String sMongoDBIP = PropertiesHelper.getValuePropertiesFile("mongodb.properties","mongoDBIP");
      Integer iMongoDBPort = Integer.parseInt(PropertiesHelper.getValuePropertiesFile("mongodb.properties","mongoDBPort"));
      String sMongoDBName = PropertiesHelper.getValuePropertiesFile("mongodb.properties","mongoDBName");
      MongoClient mongoClient = new MongoClient(sMongoDBIP,iMongoDBPort);
      DB db = mongoClient.getDB(sMongoDBName);
      DBCollection collection = db.getCollection(iTestResult.getName());
      BasicDBObject document = new BasicDBObject("RunId", sRunId)
          .append("StartTime",lRunInitiatedAt)
          .append("Name", iTestResult.getName())
          .append("Status" , iTestResult.getStatus())
          .append("TimeElapsed", iTestResult.getEndMillis()-iTestResult.getStartMillis())
          .append("hostname",sHostName );
      collection.insert(document);
      extentTest.log(Status.INFO, "logging results into mongo against run_id "+sRunId);
    }
  }


  @AfterSuite
  public void afterSuite(ITestContext testContext) {
    extentReports.flush();
  }

  public void assertTrue(boolean bool, String passMessage, String failMessage) {
    ExtentTest extentTest = ExtentTestManager.getExtentTest();
    if (bool) {
      extentTest.log(Status.PASS, "passed: " +"<pre>"+ passMessage+"</pre>");
    } else {
      extentTest.log(Status.FAIL, "failed: " +"<pre>"+failMessage+"</pre>");
    }
    Assert.assertTrue(bool, "<pre>"+failMessage+"</pre>");
  }


  public void assertFalse(boolean bool, String passMessage, String failMessage) {
    ExtentTest extentTest = ExtentTestManager.getExtentTest();
    if (!bool) {
      extentTest.log(Status.PASS, "passed: " + passMessage);
    } else {
      extentTest.log(Status.FAIL, "failed: " + failMessage);
    }
    Assert.assertFalse(bool, failMessage);
  }

  /**
   * @deprecated Replaced by {@link #assertEqual(String, String, String)}
   */
  @Deprecated
  public void assertEqual(String observedValue, String referenceValue, String passMessage,
      String failMessage) {
    if (referenceValue.contains("*")) {
      boolean equalityFlag = ValidationsHelper
          .compareStringsWithWildcards(observedValue, referenceValue);
      if (equalityFlag) {
        assertTrue(true, passMessage, "");
      } else {
        assertTrue(false, "",
            failMessage + "Expected : " + referenceValue + " but Observed: " + observedValue);
      }
    } else {
      if (observedValue.equals(referenceValue)) {
        assertTrue(true, passMessage, "");
      } else {
        assertTrue(false, "",
            failMessage + "Expected : " + referenceValue + " but Observed: " + observedValue);
      }
    }
  }

  public void assertEqual(String observedValue, String referenceValue, String key) {
    String passMessage =
        key + " value for key matched with expected . Observed " + observedValue + " Expected "
            + referenceValue;
    String failMessage =
        key + " value for key did not match with expected . Observed " + observedValue
            + " Expected " + referenceValue;
    if (referenceValue.contains("*")) {
      boolean equalityFlag = ValidationsHelper
          .compareStringsWithWildcards(observedValue, referenceValue);
      if (equalityFlag) {
        assertTrue(true, passMessage, "");
      } else {
        assertTrue(false, "", failMessage);
      }
    } else {
      if (observedValue.equals(referenceValue)) {
        assertTrue(true, passMessage, "");
      } else {
        assertTrue(false, "", failMessage);
      }
    }
  }

  /**
   * @deprecated Replaced by {@link #assertContains(ArrayList, ArrayList, String)}
   */
  @Deprecated
  public void assertEqual(List<String> observedListOfValues, List<String> referenceListOfValues, String key){
    List<String> observedListCopy = new ArrayList<>(observedListOfValues);
    List<String> referenceListCopy = new ArrayList<>(referenceListOfValues);
    referenceListCopy.removeAll(observedListCopy);
    int noOfItemsExpected = observedListCopy.size()-referenceListOfValues.size();
    String passMessage = key+" value for key matched with expected . \nObserved :"+observedListOfValues+" \nExpected :"+referenceListOfValues;
    String failMessage = key+" value for key did not match with expected . \nObserved :"+observedListOfValues+" \nExpected :"+referenceListOfValues + "\nFollowing Items did not match :"+referenceListCopy;

    observedListCopy.removeAll(referenceListOfValues);

    if(observedListOfValues.size()!=0) { //handle empty array
      if (observedListCopy.size() == noOfItemsExpected) {
        assertTrue(true, passMessage, "");
      } else {
        assertTrue(false, "", failMessage);
      }
    }
    else {
      int referenceListValueLength = referenceListOfValues.get(0).length();
      if((referenceListOfValues.size()==1) && referenceListValueLength == 0){
        assertTrue(true, passMessage, "");
      }
      else{
        assertTrue(false, "", failMessage);
      }
    }
  }

  public void assertContains(ArrayList<String> observedListOfValues, ArrayList<String> referenceListOfValues, String key){
    List<String> observedListCopy = new ArrayList<>(observedListOfValues);
    List<String> referenceListCopy = new ArrayList<>(referenceListOfValues);
    referenceListCopy.removeAll(observedListCopy);
    String passMessage = key+" value for key matched with expected . \nObserved :"+observedListOfValues+" \nExpected :"+referenceListOfValues;
    String failMessage = key+" value for key did not match with expected . \nObserved :"+observedListOfValues+" \nExpected :"+referenceListOfValues + "\nFollowing Items did not match :"+referenceListCopy;
    if(referenceListCopy.size()==0){
      assertTrue(true, passMessage, "");
    }
    else{
      assertTrue(false, "", failMessage);
    }
  }

  public void assertContains(String observedValue, String referenceValue, String passMessage,
      String failMessage) {
    if (referenceValue.contains("*")) {
      boolean equalityFlag = ValidationsHelper
          .compareStringsWithWildcards(observedValue, referenceValue);
      if (equalityFlag) {
        assertTrue(true, passMessage, "");
      } else {
        assertTrue(false, "",
            failMessage + "Expected : " + referenceValue + " but Observed: " + observedValue);
      }
    } else {
      if (observedValue.contains(referenceValue)) {
        assertTrue(true, passMessage, "");
      } else {
        assertTrue(false, "",
            failMessage + "Expected : " + referenceValue + " but Observed: " + observedValue);
      }
    }
  }

  public void assertContains(String observedValue, String referenceValue, String sKey) {
    String passMessage =
        sKey + " value for key matched with expected . Observed " + observedValue + " Expected "
            + referenceValue;
    String failMessage =
        sKey + " value for key did not match with expected . Observed " + observedValue
            + " Expected " + referenceValue;
    if (referenceValue.contains("*")) {
      boolean equalityFlag = ValidationsHelper
          .compareStringsWithWildcards(observedValue, referenceValue);
      if (equalityFlag) {
        assertTrue(true, passMessage, "");
      } else {
        assertTrue(false, "",
            failMessage + "Expected : " + referenceValue + " but Observed: " + observedValue);
      }
    } else {
      if (observedValue.contains(referenceValue)) {
        assertTrue(true, passMessage, "");
      } else {
        assertTrue(false, "",
            failMessage + "Expected : " + referenceValue + " but Observed: " + observedValue);
      }
    }
  }

  public void assertNotEqual(String observedValue, String referenceValue, String passMessage,
      String failMessage) {
    if (referenceValue.contains("*")) {
      boolean equalityFlag = ValidationsHelper
          .compareStringsWithWildcards(observedValue, referenceValue);
      if (equalityFlag) {
        assertTrue(false, "",
            failMessage + "Expected : " + referenceValue + " but Observed: " + observedValue);
      } else {
        assertTrue(true, passMessage, "");
      }
    } else {
      if (observedValue.equals(referenceValue)) {
        assertTrue(false, "",
            failMessage + "Expected : " + referenceValue + " but Observed: " + observedValue);
      } else {
        assertTrue(true, passMessage, "");
      }
    }
  }

  public void assertNotEqual(Integer observedValue, Integer referenceValue, String passMessage,
      String failMessage) {
    if (observedValue == referenceValue) {
      assertTrue(false, "", failMessage);
    } else {
      assertTrue(true, passMessage, "");
    }
  }

  public <T> void assertNodePresence(T t, String nodeName, String passMessage, String failMessage) {
    JSONObject jsonObj = new JSONObject(t);
    boolean isNodePresent = jsonObj.has(nodeName);
    if (isNodePresent) {
      assertTrue(true, passMessage, "");
    } else {
      assertTrue(false, "", failMessage);
    }
  }


  public void logResponse(Response response) {
    String sPrettyJSONResponse = "";
    try {
      JSONObject jsonObject = new JSONObject(response.body().asString());
      sPrettyJSONResponse = jsonObject.toString(4);
    } catch (org.json.JSONException jsonException) {
      sPrettyJSONResponse = response.body().asString();
    }
    ExtentTest extentTest = ExtentTestManager.getExtentTest();
    extentTest
        .log(Status.INFO, "API Response: \n" + "<pre>" + sPrettyJSONResponse + "</pre>" + "\n");
  }

  public void logInfo(String sInfo) {
    ExtentTest extentTest = ExtentTestManager.getExtentTest();
    extentTest.log(Status.INFO, sInfo);
  }

  public void logInfoPreservingFormat(String sInfo) {
    ExtentTest extentTest = ExtentTestManager.getExtentTest();
    extentTest.log(Status.INFO, "<pre>" + sInfo + "</pre>");
  }

  public void setSuiteContextValue(String sKey, String sValue, ITestContext testContext) {
    testContext.getSuite().setAttribute(sKey, sValue);
    ExtentTest extentTest = ExtentTestManager.getExtentTest();
    extentTest.log(Status.INFO,
        "setting " + sKey + " as attribute with value " + sValue + " in suite-context");
  }

  public void setSuiteContextValue(String sKey, Object oValue, ITestContext testContext) {
    testContext.getSuite().setAttribute(sKey, oValue);
    ExtentTest extentTest = ExtentTestManager.getExtentTest();
    extentTest.log(Status.INFO,
        "setting " + sKey + " as attribute with value " + oValue.toString() + " in suite-context");
  }

  public String getSuiteContextValue(String sKey, ITestContext testContext) {
    ExtentTest extentTest = ExtentTestManager.getExtentTest();
    String sValue = "";
    Object storedObject = testContext.getSuite().getAttribute(sKey);
    if (storedObject != null) {
      sValue = testContext.getSuite().getAttribute(sKey).toString();
      extentTest.log(Status.INFO,
          "retrieving " + sValue + " as value against attribute " + sKey + " from suite-context");
    } else {
      extentTest.fail("test-context value not found for key " + sKey);
    }
    return sValue;
  }

  public String getMapAsString(Map<String, String> map) {
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<String, String> entry = iterator.next();
      stringBuilder.append(entry.getKey());
      stringBuilder.append('=').append('"');
      stringBuilder.append(entry.getValue());
      stringBuilder.append('"');
      if (iterator.hasNext()) {
        stringBuilder.append(',').append('\n').append(' ');
      }
    }
    return stringBuilder.toString();
  }

  public String generateRandomString(int count) {
    String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder builder = new StringBuilder();
    while (count-- != 0) {
      int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
      builder.append(ALPHA_NUMERIC_STRING.charAt(character));
    }
    return builder.toString();
  }

}
