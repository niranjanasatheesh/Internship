package attrqa.maestro.Mar19Release.api;

import attrqa.framework.base.APITestBase;
import attrqa.framework.dataProvider.TestDataProvider;
import attrqa.framework.helper.KeywordsHelper;
import attrqa.framework.helper.PropertiesHelper;
import attrqa.framework.helper.ValidationsHelper;
import attrqa.framework.viewmodels.response.maestro.GetClientModuleMappingResponseVM;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Test_Maestro_GetClientModuleMapping extends APITestBase {

  String sHost;
  String sEndpoint;

  @BeforeClass
  public void getEnvironment(ITestContext testContext) throws IOException {
    sHost = PropertiesHelper
        .getHostStringFromPropertiesFile(sPropertyFileName, Test_Maestro_GetClientV1.class);
    sEndpoint = "/admin/client/{mieClientId}/{module}/config";
  }


  @Test(dataProvider = "dataProvider", dataProviderClass = TestDataProvider.class)
  public void getClientModuleMapping(Map<Object, Object> testData, ITestContext testContext)
      throws IOException {
    String contentType = KeywordsHelper
        .replaceKeyword(testData.get("contenttype").toString(), testContext);
    String mieClientId = KeywordsHelper
        .replaceKeyword(testData.get("mieclientid").toString(), testContext);
    String module = KeywordsHelper.replaceKeyword(testData.get("module").toString(), testContext);
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
    pathParameterMap.put("mieClientId",mieClientId);
    pathParameterMap.put("module",module);

    RequestSpecification request = RestAssured.given().headers(headerMap).pathParams(pathParameterMap);
    Response response = request.get(sHost + sEndpoint);

    logInfo("Endpoint URL: " + sHost + sEndpoint);
    logResponse(response);

    boolean isResponseCodeCorrect = (response.getStatusCode() == Integer.parseInt(responseCode));
    assertTrue(isResponseCodeCorrect, "API response status code is  expected. \nExpected :" + Integer.parseInt(responseCode)
                    + "\nObserved : " + response.getStatusCode(),
            "API response status code is not as expected. \nExpected : " + Integer.parseInt(responseCode)
                    + "\nObserved : " + response.getStatusCode());

    if (String.valueOf(response.getStatusCode()).startsWith("2") && responseCode.startsWith("2")) {
      GetClientModuleMappingResponseVM currResponse = response
              .as(GetClientModuleMappingResponseVM.class);

      if (!(successResponse.trim().equals(""))) {
        successResponse = KeywordsHelper.replaceKeyword(successResponse, testContext);
        String[] createClientElementsToVerify = successResponse.split("~");
        for (int i = 0; i < createClientElementsToVerify.length; i++) {
          String paramStringArray = createClientElementsToVerify[i];
          String paramKey = paramStringArray.substring(0, paramStringArray.indexOf(":"));
          String paramValue = paramStringArray.substring(paramStringArray.indexOf(":") + 1);
          // String crtClientParamIdValue = paramStringArray.trim();
          switch (paramKey.toLowerCase().trim()) {

            case "mieclientid":
              assertEqual(currResponse.getMieClientId(), paramValue, "mieClientId");
              break;

            case "refreshcadence":
              assertEqual(currResponse.getRefreshCadence(), paramValue, "refreshCadence");
              break;

            case "lookbackwindowdays":
              assertEqual(currResponse.getLookbackWindowDays(), paramValue, "lookbackWindowDays");
              break;

            case "refreshdelaydays":
              assertEqual(currResponse.getRefreshDelayDays(), paramValue, "refreshDelayDays");
              break;

            case "fiscalweekstart":
              assertEqual(currResponse.getFiscalWeekStart(), paramValue, "fiscalWeekStart");
              break;

            case "refreshstarttime":
              assertEqual(currResponse.getRefreshStartTime(), paramValue, "refreshStartTime");
              break;

            case "createdbyuserid":
              assertEqual(currResponse.getCreatedByUserId(), paramValue, "createdByUserId");
              break;
          }

        }

      }
    }

    if (!(errorResponse.trim().equals(""))) {

      String partsKey = errorResponse.substring(0, errorResponse.indexOf(":"));
      String partsValue = errorResponse.substring(errorResponse.indexOf(":") + 1);
      JsonPath jsonpath = response.jsonPath();

      String observedMessage = jsonpath.get(partsKey);
      String expectedMessage = partsValue;

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

