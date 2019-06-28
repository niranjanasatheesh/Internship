package attrqa.pii.Mar19Release.api;

import attrqa.framework.base.APITestBase;
import attrqa.framework.dataProvider.TestDataProvider;
import attrqa.framework.helper.KeywordsHelper;
import attrqa.framework.helper.PropertiesHelper;
import attrqa.framework.viewmodels.response.pii.BatchAPIResponse;
import attrqa.framework.viewmodels.response.pii.FacebookPIIPayload;
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

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Test_PII_BatchAPI extends APITestBase {

  String sHost;
  String sEndPoint;

  @BeforeClass
  public void getEnvironment(ITestContext testContext)
      throws IOException {
    sHost = PropertiesHelper
        .getHostStringFromPropertiesFile(sPropertyFileName, Test_PII_BatchAPI.class);
    sEndPoint = "/v1/pii/batch";
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
      reader = new BufferedReader(new FileReader("E:\\Niranjana\\New Folder\\mtc.txt"));//manual test cases read and wrote to specific field in db
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
  public void test_BatchAPI(Map<Object, Object> testData, ITestContext testContext)
      throws IOException {

    String sContentType = KeywordsHelper
        .replaceKeyword(testData.get("content-type").toString(), testContext);
    String sx_apigw_api_id = KeywordsHelper
        .replaceKeyword(testData.get("x_apigw_api_id").toString(), testContext);
//    String sx_apigw_api_id = "7woe4y7xhd";
    String sBucket = KeywordsHelper
        .replaceKeyword(testData.get("bucket").toString(), testContext);
    String sKey = KeywordsHelper
        .replaceKeyword(testData.get("key").toString(), testContext);
    String sFile = KeywordsHelper
        .replaceKeyword(testData.get("file").toString(), testContext);
    String sRegion = KeywordsHelper
        .replaceKeyword(testData.get("region").toString(), testContext);
    String sExpectedEncodedEmail = KeywordsHelper
        .replaceKeyword(testData.get("validation_email").toString(), testContext);
    String sExpectedEncodedPhone = KeywordsHelper
        .replaceKeyword(testData.get("validation_phone").toString(), testContext);
    String sExpectedEncodedGen = KeywordsHelper
        .replaceKeyword(testData.get("validation_gen").toString(), testContext);
    String sExpectedEncodedDOBY = KeywordsHelper
        .replaceKeyword(testData.get("validation_doby").toString(), testContext);
    String sExpectedEncodedDOBM = KeywordsHelper
        .replaceKeyword(testData.get("validation_dobm").toString(), testContext);
    String sExpectedEncodedDOBD = KeywordsHelper
        .replaceKeyword(testData.get("validation_dobd").toString(), testContext);
    String sExpectedEncodedFN = KeywordsHelper
        .replaceKeyword(testData.get("validation_fn").toString(), testContext);
    String sExpectedEncodedLN = KeywordsHelper
        .replaceKeyword(testData.get("validation_ln").toString(), testContext);
    String sExpectedEncodedFI = KeywordsHelper
        .replaceKeyword(testData.get("validation_fi").toString(), testContext);
    String sExpectedEncodedST = KeywordsHelper
        .replaceKeyword(testData.get("validation_st").toString(), testContext);
    String sExpectedEncodedCT = KeywordsHelper
        .replaceKeyword(testData.get("validation_ct").toString(), testContext);
    String sExpectedEncodedZIP = KeywordsHelper
        .replaceKeyword(testData.get("validation_zip").toString(), testContext);
    String sExpectedEncodedCountry = KeywordsHelper
        .replaceKeyword(testData.get("validation_country").toString(), testContext);
    String sExpectedEncodedMADID = KeywordsHelper
        .replaceKeyword(testData.get("validation_madid").toString(), testContext);
    String sExpectedEncodedEXTERNID = KeywordsHelper
        .replaceKeyword(testData.get("validation_extern_id").toString(), testContext);
    String sFieldwiseQualityEmail = KeywordsHelper
        .replaceKeyword(testData.get("fieldwisequality_email").toString(), testContext);
    String sFieldwiseQualityDateofBirth=KeywordsHelper
        .replaceKeyword(testData.get("fieldwisequality_date_of_birth").toString(), testContext);
    String sFieldwiseQualityLastName=KeywordsHelper
        .replaceKeyword(testData.get("fieldwisequality_last_name").toString(), testContext);
    String sFieldwiseQualityFirstName=KeywordsHelper
        .replaceKeyword(testData.get("fieldwisequality_first_name").toString(), testContext);
    String sFieldwiseQualityUSState=KeywordsHelper
        .replaceKeyword(testData.get("fieldwisequality_us_state").toString(), testContext);
    String sFieldwiseQualityCity=KeywordsHelper
        .replaceKeyword(testData.get("fieldwisequality_city").toString(), testContext);
    String sFieldwiseQualityZip=KeywordsHelper
        .replaceKeyword(testData.get("fieldwisequality_zip").toString(), testContext);
    String sFieldwiseQualityCountryCode=KeywordsHelper
        .replaceKeyword(testData.get("fieldwisequality_country_code").toString(), testContext);
    String sFieldwiseQualityMobileDeviceId=KeywordsHelper
        .replaceKeyword(testData.get("fieldwisequality_mobile_device_id").toString(), testContext);
    String sFieldwiseQualityUserid=KeywordsHelper
        .replaceKeyword(testData.get("fieldwisequality_userid").toString(), testContext);

    Map<String, String> headerMap = new HashMap<>();
    headerMap.put("Content-Type", sContentType);
    headerMap.put("x-apigw-api-id",sx_apigw_api_id);

    Map<String, String> queryParameterMap = new HashMap<>();
    queryParameterMap.put("bucket", sBucket);
    queryParameterMap.put("key", sKey + "/" + sFile);
    queryParameterMap.put("region", sRegion);

    RequestSpecification requestSpecification = RestAssured.given().headers(headerMap)
        .queryParams(queryParameterMap);
    Response response = requestSpecification.get(sHost + sEndPoint);
    logInfo("EndPoint :" + sHost + sEndPoint);
    logResponse(response);

    BatchAPIResponse batchAPIResponse = response.as(BatchAPIResponse.class);

    FacebookPIIPayload facebookPIIPayload = batchAPIResponse.getFacebookPIIPayload();
    List<String> facebookPIIPayloadSchema = facebookPIIPayload.getSchema();
    List<List<String>> facebookPIIPayloadData = facebookPIIPayload.getData();

    int index = 0;
    Map<Integer, String> indexSchemaMap = new HashMap<>();
    Map<String, ArrayList<String>> schemaDataListMap = new HashMap<>();
    for (String schemaElement : facebookPIIPayloadSchema) {
      ArrayList<String> tempList = new ArrayList<>();
      indexSchemaMap.put(index, schemaElement);
      schemaDataListMap.put(schemaElement, tempList);
      index++;
    }

    Iterator<List<String>> iteratorOnEncodedRows = facebookPIIPayloadData.iterator();
    while (iteratorOnEncodedRows.hasNext()) {
      List<String> encodedRow = iteratorOnEncodedRows.next();
      int schemaIndex = 0;
      for (String encodedColumn : encodedRow) {
        ArrayList<String> tempList = schemaDataListMap.get(indexSchemaMap.get(schemaIndex));
        tempList.add(encodedColumn);
        schemaDataListMap.put(indexSchemaMap.get(schemaIndex), tempList);
        schemaIndex++;
      }
    }

    if (!sExpectedEncodedEmail.trim().equalsIgnoreCase("")) {
      ArrayList<String> listOfExpectedEncodedValues = getStringAsList(sExpectedEncodedEmail);
      ArrayList<String> listOfObservedEncodedValues = schemaDataListMap.get("EMAIL");
      assertContains(listOfObservedEncodedValues, listOfExpectedEncodedValues, "EMAIL");
    }

    if (!sExpectedEncodedPhone.trim().equalsIgnoreCase("")) {
      ArrayList<String> listOfExpectedEncodedValues = getStringAsList(sExpectedEncodedPhone);
      ArrayList<String> listOfObservedEncodedValues = schemaDataListMap.get("PHONE");
      assertContains(listOfObservedEncodedValues, listOfExpectedEncodedValues, "PHONE");
    }

    if (!sExpectedEncodedGen.trim().equalsIgnoreCase("")) {
      ArrayList<String> listOfExpectedEncodedValues = getStringAsList(sExpectedEncodedGen);
      ArrayList<String> listOfObservedEncodedValues = schemaDataListMap.get("GEN");
      assertContains(listOfObservedEncodedValues, listOfExpectedEncodedValues, "GEN");
    }

    if (!sExpectedEncodedDOBY.trim().equalsIgnoreCase("")) {
      ArrayList<String> listOfExpectedEncodedValues = getStringAsList(sExpectedEncodedDOBY);
      ArrayList<String> listOfObservedEncodedValues = schemaDataListMap.get("DOBY");
      assertContains(listOfObservedEncodedValues, listOfExpectedEncodedValues, "DOBY");
    }

    if (!sExpectedEncodedDOBM.trim().equalsIgnoreCase("")) {
      ArrayList<String> listOfExpectedEncodedValues = getStringAsList(sExpectedEncodedDOBM);
      ArrayList<String> listOfObservedEncodedValues = schemaDataListMap.get("DOBM");
      assertContains(listOfObservedEncodedValues, listOfExpectedEncodedValues, "DOBM");
    }

    if (!sExpectedEncodedDOBD.trim().equalsIgnoreCase("")) {
      ArrayList<String> listOfExpectedEncodedValues = getStringAsList(sExpectedEncodedDOBD);
      ArrayList<String> listOfObservedEncodedValues = schemaDataListMap.get("DOBD");
      assertContains(listOfObservedEncodedValues, listOfExpectedEncodedValues, "DOBD");
    }

    if (!sExpectedEncodedFN.trim().equalsIgnoreCase("")) {
      ArrayList<String> listOfExpectedEncodedValues = getStringAsList(sExpectedEncodedFN);
      ArrayList<String> listOfObservedEncodedValues = schemaDataListMap.get("FN");
      assertContains(listOfObservedEncodedValues, listOfExpectedEncodedValues, "FN");
    }

    if (!sExpectedEncodedLN.trim().equalsIgnoreCase("")) {
      ArrayList<String> listOfExpectedEncodedValues = getStringAsList(sExpectedEncodedLN);
      ArrayList<String> listOfObservedEncodedValues = schemaDataListMap.get("LN");
      assertContains(listOfObservedEncodedValues, listOfExpectedEncodedValues, "LN");
    }

    if (!sExpectedEncodedFI.trim().equalsIgnoreCase("")) {
      ArrayList<String> listOfExpectedEncodedValues = getStringAsList(sExpectedEncodedFI);
      ArrayList<String> listOfObservedEncodedValues = schemaDataListMap.get("FI");
      assertContains(listOfObservedEncodedValues, listOfExpectedEncodedValues, "FI");
    }

    if (!sExpectedEncodedST.trim().equalsIgnoreCase("")) {
      ArrayList<String> listOfExpectedEncodedValues = getStringAsList(sExpectedEncodedST);
      ArrayList<String> listOfObservedEncodedValues = schemaDataListMap.get("ST");
      assertContains(listOfObservedEncodedValues, listOfExpectedEncodedValues, "ST");
    }

    if (!sExpectedEncodedCT.trim().equalsIgnoreCase("")) {
      ArrayList<String> listOfExpectedEncodedValues = getStringAsList(sExpectedEncodedCT);
      ArrayList<String> listOfObservedEncodedValues = schemaDataListMap.get("CT");
      assertContains(listOfObservedEncodedValues, listOfExpectedEncodedValues, "CT");
    }

    if (!sExpectedEncodedZIP.trim().equalsIgnoreCase("")) {
      ArrayList<String> listOfExpectedEncodedValues = getStringAsList(sExpectedEncodedZIP);
      ArrayList<String> listOfObservedEncodedValues = schemaDataListMap.get("ZIP");
      assertContains(listOfObservedEncodedValues, listOfExpectedEncodedValues, "ZIP");
    }

    if (!sExpectedEncodedCountry.trim().equalsIgnoreCase("")) {
      ArrayList<String> listOfExpectedEncodedValues = getStringAsList(sExpectedEncodedCountry);
      ArrayList<String> listOfObservedEncodedValues = schemaDataListMap.get("COUNTRY");
      assertContains(listOfObservedEncodedValues, listOfExpectedEncodedValues, "COUNTRY");
    }

    if (!sExpectedEncodedMADID.trim().equalsIgnoreCase("")) {
      ArrayList<String> listOfExpectedEncodedValues = getStringAsList(sExpectedEncodedMADID);
      ArrayList<String> listOfObservedEncodedValues = schemaDataListMap.get("MADID");
      assertContains(listOfObservedEncodedValues, listOfExpectedEncodedValues, "MADID");
    }

    if (!sExpectedEncodedEXTERNID.trim().equalsIgnoreCase("")) {
      ArrayList<String> listOfExpectedEncodedValues = getStringAsList(sExpectedEncodedEXTERNID);
      ArrayList<String> listOfObservedEncodedValues = schemaDataListMap.get("EXTERN_ID");
      assertContains(listOfObservedEncodedValues, listOfExpectedEncodedValues, "EXTERN_ID");
    }


    if(!sFieldwiseQualityEmail.equalsIgnoreCase("")){
      String sObservedFieldwiseQualityEmail = String.valueOf(batchAPIResponse.getFieldwiseQuality().geteMAIL());
      assertEqual(sObservedFieldwiseQualityEmail,sFieldwiseQualityEmail,"fieldwiseQuality:Email");
    }

    if(!sFieldwiseQualityDateofBirth.equalsIgnoreCase("")){
      String sObservedFieldwiseQualityDOB = String.valueOf(batchAPIResponse.getFieldwiseQuality().getdATE_OF_BIRTH());
      assertEqual(sObservedFieldwiseQualityDOB,sFieldwiseQualityDateofBirth,"fieldwiseQuality:Date of Birth");
    }

    if(!sFieldwiseQualityLastName.equalsIgnoreCase("")){
      String sObservedFieldwiseQualityLastName = String.valueOf(batchAPIResponse.getFieldwiseQuality().getlAST_NAME());
      assertEqual(sObservedFieldwiseQualityLastName,sFieldwiseQualityLastName,"fieldwiseQuality:Last Name");
    }

    if(!sFieldwiseQualityFirstName.equalsIgnoreCase("")){
      String sObservedFieldwiseQualityFirstName = String.valueOf(batchAPIResponse.getFieldwiseQuality().getfIRST_NAME());
      assertEqual(sObservedFieldwiseQualityFirstName,sFieldwiseQualityFirstName,"fieldwiseQuality:First Name");
    }

    if(!sFieldwiseQualityUSState.equalsIgnoreCase("")){
      String sObservedFieldwiseQualityUSState = String.valueOf(batchAPIResponse.getFieldwiseQuality().getuS_STATE());
      assertEqual(sObservedFieldwiseQualityUSState,sFieldwiseQualityUSState,"fieldwiseQuality:US State");
    }

    if(!sFieldwiseQualityCity.equalsIgnoreCase("")){
      String sObservedFieldwiseQualityCity = String.valueOf(batchAPIResponse.getFieldwiseQuality().getcITY());
      assertEqual(sObservedFieldwiseQualityCity,sFieldwiseQualityCity,"fieldwiseQuality:City");
    }

    if(!sFieldwiseQualityZip.equalsIgnoreCase("")){
      String sObservedFieldwiseQualityZip = String.valueOf(batchAPIResponse.getFieldwiseQuality().getzIP());
      assertEqual(sObservedFieldwiseQualityZip,sFieldwiseQualityZip,"fieldwiseQuality:zip");
    }

    if(!sFieldwiseQualityCountryCode.equalsIgnoreCase("")){
      String sObservedFieldwiseQualityCountryCode = String.valueOf(batchAPIResponse.getFieldwiseQuality().getcOUNTRY_CODE());
      assertEqual(sObservedFieldwiseQualityCountryCode,sFieldwiseQualityCountryCode,"fieldwiseQuality:Country Code");
    }

    if(!sFieldwiseQualityMobileDeviceId.equalsIgnoreCase("")){
      String sObservedFieldwiseQualityMobileDeviceId = String.valueOf(batchAPIResponse.getFieldwiseQuality().getmOBILE_DEVICE_ID());
      assertEqual(sObservedFieldwiseQualityMobileDeviceId,sFieldwiseQualityMobileDeviceId,"fieldwiseQuality:Mobile Device Id");
    }

    if(!sFieldwiseQualityUserid.equalsIgnoreCase("")){
      String sObservedFieldwiseQualityUserid = String.valueOf(batchAPIResponse.getFieldwiseQuality().getuSER_ID());
      assertEqual(sObservedFieldwiseQualityUserid,sFieldwiseQualityUserid,"fieldwiseQuality:User Id");
    }

  }


  public ArrayList<String> getStringAsList(String sExpectedEncodedValues) {
    ArrayList<String> listOfExpectedEncodedValues = new ArrayList<>();
    String[] items = sExpectedEncodedValues.split("~");
    for (int i = 0; i < items.length; i++) {
      String[] parts = items[i].split("=");
      String key = parts[0];
      String value = parts[1];
      listOfExpectedEncodedValues.add(value);
    }
    return listOfExpectedEncodedValues;
  }




}
