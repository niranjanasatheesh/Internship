package attrqa.optimusprime.Mar19Release.api;

import attrqa.framework.base.APITestBase;
import attrqa.framework.dataProvider.TestDataProvider;
import attrqa.framework.helper.KeywordsHelper;
import attrqa.framework.helper.PropertiesHelper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static attrqa.framework.helper.AWSHelper.queryAthena;

public class Test_Optimus_BubbleChart extends APITestBase {


  private String sHost;

  @BeforeClass
  public void getEnvironment(ITestContext testContext)
      throws IOException {
    sHost = PropertiesHelper
        .getHostStringFromPropertiesFile(sPropertyFileName, Test_Optimus_BubbleChart.class);
  }


  @AfterMethod
  public void insertmongo(ITestResult result) {
    String status = null;
    if(result.isSuccess()){
      status = "Pass";
    }
    else{
      status = "Fail";
    }
    String name = result.getName();
    String Hostname = null;
    try {
      Hostname = InetAddress.getLocalHost().getHostName();
    }catch(Exception ex){}
    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());


    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader("E:\\Niranjana\\New Folder\\mtc.txt"));
      String line1 = reader.readLine();
      String summary = null;


      while (line1 != null) {


        String csvFile = "E:\\Niranjana\\New Folder\\JIRA.txt";
        BufferedReader br = null;
        String line2 = "";
        String csvSplitBy1 = ",";

        br = new BufferedReader(new FileReader(csvFile));
        while ((line2 = br.readLine()) != null) {

          String[] data = line2.split(csvSplitBy1);
          String mtc = data[0];
          String atc = data[1];
          if (mtc.equals(line1)) {
            String[] mname = atc.split("#");
            if(mname[1].equals(name)){

              String csvFile1 = "E:\\Niranjana\\New Folder\\testcases1.txt";
              BufferedReader br1 = null;
              String line = "";
              String csvSplitBy = ",";

              br1 = new BufferedReader(new FileReader(csvFile1));
              while ((line = br1.readLine()) != null) {

                String[] data1 = line.split(csvSplitBy);
                String open1 = data1[0];
                if (open1.equals(line1)) {
                  summary = data1[1];
                  break;
                }
              }
              MongoClient mongoClient = new MongoClient( "10.60.163.75" , 27017 );
              DB db = mongoClient.getDB( "logOfResults" );
              DBCollection coll = db.getCollection(name);
              BasicDBObject doc = new BasicDBObject("Test name", name)
                      .append("Summary" , summary)
                      .append("Time" , timeStamp)
                      .append("Hostname" , Hostname)
                      .append("Status" , status);
              coll.insert(doc);
              line1 = reader.readLine();
            }

          }

        }}
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Test(dataProvider = "dataProvider", dataProviderClass = TestDataProvider.class)
  public void getDataFromBubbleChart(Map<Object, Object> testData, ITestContext testContext)
      throws Exception {

    String authorizationKey = KeywordsHelper
        .replaceKeyword(testData.get("authorizationkey").toString(), testContext);
    String brandKey = KeywordsHelper
        .replaceKeyword(testData.get("brandkey").toString(), testContext);
    String view = KeywordsHelper
        .replaceKeyword(testData.get("view").toString(), testContext);
    String clientKey = KeywordsHelper
        .replaceKeyword(testData.get("clientkey").toString(), testContext);
    String responseCode = KeywordsHelper
        .replaceKeyword(testData.get("responsecode").toString(), testContext);
    String title = KeywordsHelper
        .replaceKeyword(testData.get("title").toString(), testContext);
    String metricTitle = KeywordsHelper
        .replaceKeyword(testData.get("metrictitle").toString(), testContext);
    String y = KeywordsHelper
        .replaceKeyword(testData.get("y").toString(), testContext);
    String radius = KeywordsHelper
        .replaceKeyword(testData.get("radius").toString(), testContext);
    String gradient = KeywordsHelper
        .replaceKeyword(testData.get("gradient").toString(), testContext);

    System.out.println("metric title : " + metricTitle);

    Map<String, String> headersMap = new HashMap<>();
    headersMap.put("Authorization", authorizationKey);

    Map<String, String> queryParamsMap = new HashMap<>();
    queryParamsMap.put("clientKey", clientKey);
    queryParamsMap.put("brandKey", brandKey);
    queryParamsMap.put("view", view);

    RequestSpecification request = RestAssured.given().headers(headersMap)
        .queryParams(queryParamsMap);
    String sEndPoint = "scenario-runs/latest";
    Response response = request.get(sHost + sEndPoint);
    logInfo("endpoint: " + sEndPoint);
    logResponse(response);

    System.out.println(response);

    String sJSON = response.body().asString();
    DocumentContext documentContext = JsonPath.parse(sJSON);

    //RESPONSE CODE
    boolean isResponseCodeCorrect = (response.getStatusCode() == Integer.parseInt(responseCode));
    assertTrue(isResponseCodeCorrect, "API response status code is as expected",
        "API response status code is not as expected. Expected : " + Integer.parseInt(responseCode)
            + " but Observed : " + response.getStatusCode());

    //DATA-TITLE VALIDATION
    // ArrayList<String> expectedListOfTitles = new ArrayList<>(Arrays.asList(title.split(",")));
    //ArrayList<Integer> expectedIntListOfTiles = new ArrayList<>();
    if (response.getStatusCode() == 200) {
      List<String> expectedListOfTitles = queryAthena("us-east-1", "dev_fancyf3",
          "s3://attr-nonprod2-useast1/EV/FANCYF3/FANCYF3/OPTIM-OUT/automation_files/",
          "select distinct multiplier from rpt_decomps_opt_forward\n"
              + "where scenario_run_id=100001");
      expectedListOfTitles.remove(0);
      List<Integer> expectedIntListOfTiles = expectedListOfTitles.stream()
          .map(s -> (Integer.parseInt(s) - 100)).collect(Collectors.toList());
      String sDataTitle = "data[*].title";
      List<String> listOfObservedValues = documentContext.read(sDataTitle);
      Pattern pattern = Pattern.compile("^(\\+|-)?\\d+");
      List<Integer> listOfExtractedInteger = new ArrayList<>();
      for (String exp : listOfObservedValues) {
        Matcher matcher = pattern.matcher(exp);
        if (matcher.find()) {
          System.out.println(matcher.group(0));
          listOfExtractedInteger.add(Integer.parseInt(matcher.group(0)));
        }
      }
      Collections.sort(listOfExtractedInteger);
      Collections.sort(expectedIntListOfTiles);
      System.out.println(expectedIntListOfTiles);
      System.out.println(listOfExtractedInteger);
      boolean titleIsEqual = expectedIntListOfTiles.equals(listOfExtractedInteger);
      System.out.println(titleIsEqual);
      assertTrue(titleIsEqual, " data.title is as expected",
          "data.title is not as expected. Expected : " + expectedIntListOfTiles
              + " but Observed : " + listOfExtractedInteger);

      //METRIC-TITLE VALIDATION
      List<String> expectedListOfMetricTitles = new ArrayList<>(
          Arrays.asList(metricTitle.split(",")));
      String sDataMetricTitle = "meta.metrics[*].title";
      List<String> listOfObservedMetricTitles = new ArrayList<>(
          documentContext.read(sDataMetricTitle));
      List<String> trimmedExpectedListOfMetricTitles =
          expectedListOfMetricTitles.stream().map(String::trim).collect(Collectors.toList());
      List<String> trimmedListOfObservedMetricTitles =
          expectedListOfMetricTitles.stream().map(String::trim).collect(Collectors.toList());
      Collections.sort(trimmedExpectedListOfMetricTitles);
      Collections.sort(trimmedListOfObservedMetricTitles);
      System.out.println(trimmedExpectedListOfMetricTitles);
      System.out.println(trimmedListOfObservedMetricTitles);
      boolean metrictitleequal = trimmedExpectedListOfMetricTitles
          .equals(trimmedListOfObservedMetricTitles);
      assertTrue(metrictitleequal, " metric.title is as expected",
          "metric.title is not as expected. Expected : " + trimmedExpectedListOfMetricTitles
              + " but Observed : " + trimmedListOfObservedMetricTitles);

      //META-Y VALIDATION
      List<String> expectedListy = new ArrayList<>(Arrays.asList(y.split(",")));
      String sMetay = "meta.y";
      List<String> observedListy = new ArrayList<>(documentContext.read(sMetay));
      List<String> trimmedExpectedListy =
          expectedListy.stream().map(String::trim).collect(Collectors.toList());
      List<String> trimmedObservedListy =
          observedListy.stream().map(String::trim).collect(Collectors.toList());
      Collections.sort(trimmedExpectedListy);
      Collections.sort(trimmedObservedListy);
      boolean metayequal = trimmedExpectedListy.equals(trimmedObservedListy);
      assertTrue(metayequal, " meta.y is as expected",
          "meta.y is not as expected. Expected : " + trimmedExpectedListy
              + " but Observed : " + trimmedObservedListy);

      //META-RADIUS VALIDATION

      List<String> expectedListradius = new ArrayList<>(Arrays.asList(radius.split(",")));
      String sRadius = "meta.radius";
      List<String> observedListradius = new ArrayList<>(documentContext.read(sRadius));
      boolean Radiusequal = expectedListradius.equals(observedListradius);
      assertTrue(Radiusequal, " meta.radius is as expected",
          "meta.radius is not as expected. Expected : " + expectedListradius
              + " but Observed : " + observedListradius);

      try {
        List<String> list = queryAthena("us-east-1", "feature_fancyf3",
            "s3://attr-nonprod2-useast1/EV/FANCYF3/FANCYF3/OPTIM-OUT/automation_files/",
            "SELECT sum(spend),\tsum(exposure) FROM \"feature_fancyf3\".\"rpt_decomps_opt_forward\" where multiplier=90");
        System.out.println(list.get(0));
        System.out.println(list.get(1));
        System.out.println(list.get(2));
        System.out.println(list.get(3));

      } catch (Exception e) {
        e.printStackTrace();
      }

      //META-GRADIENT VALIDATION

      List<String> expectedListgradient = new ArrayList<>(Arrays.asList(gradient.split(",")));
      String sGradient = "meta.gradient";
      List<String> observedListgradient = new ArrayList<>(documentContext.read(sGradient));

      boolean Gradientequal = expectedListgradient.equals(observedListgradient);
      assertTrue(Gradientequal, " meta.gradient is as expected",
          "meta.gradient is not as expected. Expected : " + expectedListgradient
              + " but Observed : " + observedListgradient);
    }
    else {

      boolean isResponseCodeCorrectError = (response.getStatusCode() == Integer.parseInt(responseCode));
      assertTrue(isResponseCodeCorrect, "API response status code is as expected",
          "API response status code is not as expected. Expected : " + Integer.parseInt(responseCode)
              + " but Observed : " + response.getStatusCode());

    }
  }

}
