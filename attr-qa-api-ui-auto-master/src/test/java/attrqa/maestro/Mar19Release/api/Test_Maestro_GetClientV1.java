package attrqa.maestro.Mar19Release.api;

import attrqa.framework.base.APITestBase;
import attrqa.framework.dataProvider.TestDataProvider;
import attrqa.framework.helper.KeywordsHelper;
import attrqa.framework.helper.PropertiesHelper;
import attrqa.framework.helper.ValidationsHelper;
import attrqa.framework.viewmodels.response.maestro.GetClientV1ResponseVM;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test_Maestro_GetClientV1 extends APITestBase {

  String sHost;
  String sEndpoint;

  @BeforeClass
  public void getEnvironment(ITestContext testContext) throws IOException {
    sHost = PropertiesHelper
        .getHostStringFromPropertiesFile(sPropertyFileName, Test_Maestro_GetClientV1.class);
    sEndpoint = "/admin/client/{clientId}";
  }


  @Test(dataProvider = "dataProvider", dataProviderClass = TestDataProvider.class)
  public void getClientV1(Map<Object, Object> testData, ITestContext testContext)
      throws IOException {
    String contentType = KeywordsHelper
        .replaceKeyword(testData.get("contenttype").toString(), testContext);
    String clientId = KeywordsHelper
        .replaceKeyword(testData.get("clientid").toString(), testContext);
    String responseCode = KeywordsHelper
        .replaceKeyword(testData.get("responsecode").toString(), testContext);
    String successResponse = KeywordsHelper
        .replaceKeyword(testData.get("successresponse").toString(), testContext);
    String errorResponse = KeywordsHelper
        .replaceKeyword(testData.get("errorresponse").toString(), testContext);

    Map<String, String> headerMap = new HashMap<>();
    headerMap.put("Content-Type", contentType);
    headerMap.put("Authorization", getSuiteContextValue("authorizationtoken", testContext));

    Map<String, String> pathParameterMap = new HashMap<>();
    pathParameterMap.put("clientId",clientId);

    RequestSpecification request = RestAssured.given().headers(headerMap).pathParams(pathParameterMap);
    Response response = request.get(sHost + sEndpoint);

    logInfo("Endpoint URL: " + sHost + sEndpoint);
    logResponse(response);

    boolean isResponseCodeCorrect = (response.getStatusCode() == Integer.parseInt(responseCode));
    assertTrue(isResponseCodeCorrect, "API response status code is  expected",
        "API response status code is not as expected. \nExpected : " + Integer.parseInt(responseCode)
            + "\nObserved : " + response.getStatusCode());

    if (String.valueOf(response.getStatusCode()).startsWith("2") && responseCode.startsWith("2")) {
      GetClientV1ResponseVM currResponse = response.as(GetClientV1ResponseVM.class);

      if (!(successResponse.trim().equals(""))) {
        successResponse = KeywordsHelper.replaceKeyword(successResponse, testContext);
        String[] createClientElementsToVerify = successResponse.split("~");
        for (int i = 0; i < createClientElementsToVerify.length; i++) {
          String[] paramKeyValue = createClientElementsToVerify[i].split(":");
          switch (paramKeyValue[0].toLowerCase().trim()) {

            case "cmiclientid":
              List<String> expectedListOfCmiClientIds = KeywordsHelper
                      .getStringArrayAsList(paramKeyValue[1]);
              assertEqual(currResponse.getCmiClientIds(), expectedListOfCmiClientIds, "cmiClientIds");
              break;

            case "clientcode":
              assertEqual(currResponse.getClientCode(), paramKeyValue[1], "clientCode");
              break;

            case "clientname":
              assertEqual(currResponse.getClientName(), paramKeyValue[1], "clientName");
              break;

            case "email":
              assertEqual(currResponse.getEmail(), paramKeyValue[1], "email");
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

      String[] parts = errorResponse.split(":");
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
      assertTrue(isResponseMessageCorrect, "API response observedMessage is as expected. \nExpected : " + expectedMessage + "<> Observed : " + observedMessage,
          "API response observedMessage is not as expected. Expected : " + expectedMessage
              + "\nObserved : " + observedMessage);
    }


  }

}

