package attrqa.maestro.Mar19Release.api;

import attrqa.framework.base.APITestBase;
import attrqa.framework.dataProvider.TestDataProvider;
import attrqa.framework.helper.KeywordsHelper;
import attrqa.framework.helper.PropertiesHelper;
import attrqa.framework.helper.ValidationsHelper;
import attrqa.framework.viewmodels.response.maestro.CreateClientV1ResponseVM;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.ArrayList;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test_Maestro_CreateClientV1 extends APITestBase {

  String sHost;
  String sEndpoint;

  @BeforeClass
  public void getEnvironment(ITestContext testContext) throws IOException {
    sHost = PropertiesHelper
        .getHostStringFromPropertiesFile(sPropertyFileName, Test_Maestro_CreateClientV1.class);
    sEndpoint = "/admin/client";
  }

  @Test(dataProvider = "dataProvider", dataProviderClass = TestDataProvider.class)
  public void createClientV1(Map<Object, Object> testdata, ITestContext testContext)
      throws IOException {

    String contentType = KeywordsHelper
        .replaceKeyword(testdata.get("contenttype").toString(), testContext);
    String body = KeywordsHelper.replaceKeyword(testdata.get("body").toString(), testContext);
    String responseCode = KeywordsHelper
        .replaceKeyword(testdata.get("responsecode").toString(), testContext);
    String successResponse = KeywordsHelper
        .replaceKeyword(testdata.get("successresponse").toString(), testContext);
    String errorResponse = KeywordsHelper
        .replaceKeyword(testdata.get("errorresponse").toString(), testContext);

    Map<String, String> headerMap = new HashMap<>();
    headerMap.put("Content-Type", contentType);
    headerMap.put("Authorization", getSuiteContextValue("authorizationtoken", testContext));

    RequestSpecification request = RestAssured.given().headers(headerMap);
    Response response = request.body(body).post(sHost + sEndpoint);

    logInfo("Endpoint URL: " + sHost + sEndpoint);
    logInfo("Request Body: " + body);
    logResponse(response);

    boolean isResponseCodeCorrect = (response.getStatusCode() == Integer.parseInt(responseCode));
    assertTrue(isResponseCodeCorrect, "API response status code is  expected. \nExpected :" + Integer.parseInt(responseCode)
                    + "\nObserved : " + response.getStatusCode(),
        "API response status code is not as expected.  \nExpected : " + Integer.parseInt(responseCode)
            + "\nObserved : " + response.getStatusCode());

    if (String.valueOf(response.getStatusCode()).startsWith("2") && responseCode.startsWith("2")) {
      CreateClientV1ResponseVM currResponse = response.as(CreateClientV1ResponseVM.class);

      if (!(successResponse.trim().equals(""))) {
        successResponse = KeywordsHelper.replaceKeyword(successResponse, testContext);
        String[] createClientElementsToVerify = successResponse.split("~");
        for (int i = 0; i < createClientElementsToVerify.length; i++) {
          String[] paramKeyValue = createClientElementsToVerify[i].split(":");
          switch (paramKeyValue[0].toLowerCase().trim()) {

            case "cmiclientids":

              if (paramKeyValue[1].trim().equalsIgnoreCase("null")) {
                boolean isNull = (null == currResponse.getCmiClientIds());
                assertTrue(isNull, "cmiClientIds is null as expected",
                        "cmiClientIds is not null as expected");
              } else {
                List<String> expectedListOfCmiClientIds = KeywordsHelper
                        .getStringArrayAsList(paramKeyValue[1]);
                assertContains(new ArrayList<>(currResponse.getCmiClientIds()),
                        new ArrayList<>(expectedListOfCmiClientIds), "cmiClientIds");
              }

              break;

            case "mieclientid":
              assertEqual(currResponse.getMieClientId(), paramKeyValue[1], "mieClientId");
              break;

            case "clientcode":
              assertEqual(currResponse.getClientCode(), paramKeyValue[1], "clientCode");
              break;

            case "clientname":
              assertEqual(currResponse.getClientName(), paramKeyValue[1], "clientName");
              break;

            case "email":
              if (paramKeyValue[1].trim().equalsIgnoreCase("null")) {
                boolean isNull = (null == currResponse.getEmail());
                assertTrue(isNull, "cmiClientIds is null as expected",
                        "cmiClientIds is not null as expected");
              } else {
                assertEqual(currResponse.getEmail(), paramKeyValue[1], "email");
              }
              break;

            case "createdbyuserid":
              assertEqual(currResponse.getCreatedByUserId(), paramKeyValue[1], "createdByUserId");
              break;

            case "createdattime":
              assertEqual(currResponse.getCreatedAtTime(), paramKeyValue[1], "createdAtTime");
              break;

            case "updatedbyuserid":
              assertEqual(currResponse.getUpdatedByUserId(), paramKeyValue[1], "updatedByUserId");
              break;

            case "updatedattime":
              assertEqual(currResponse.getUpdatedAtTime(), paramKeyValue[1], "updatedAtTime");
              break;

            case "version":
              assertEqual(currResponse.getVersion(), paramKeyValue[1], "version");
              break;
          }
        }
      }
    }

    if (!(errorResponse.trim().equals(""))) {

      String[] parts = errorResponse.split(":", 2);
      JsonPath jsonpath = response.jsonPath();

      String observedMessage = jsonpath.get(parts[0]);
      String expectedMessage = parts[1];

      boolean isResponseMessageCorrect;

      if (errorResponse.contains("*")) {
        isResponseMessageCorrect = ValidationsHelper
            .compareStringsWithWildcards(observedMessage, expectedMessage);
      } else {
        isResponseMessageCorrect = expectedMessage.equalsIgnoreCase(observedMessage);
      }
      assertTrue(isResponseMessageCorrect, "API response observedMessage is as expected. \nExpected : " + expectedMessage
              + "\nObserved : " + observedMessage,
          "API response observedMessage is not as expected. \nExpected : " + expectedMessage
              + "\nObserved : " + observedMessage);
    }

  }

}


