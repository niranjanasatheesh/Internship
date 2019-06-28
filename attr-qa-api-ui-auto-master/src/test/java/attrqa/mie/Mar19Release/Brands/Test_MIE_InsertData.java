package attrqa.mie.Mar19Release.Brands;

import attrqa.framework.base.APITestBase;
import attrqa.framework.dataProvider.TestDataProvider;
import attrqa.framework.helper.KeywordsHelper;
import attrqa.framework.helper.PropertiesHelper;
import attrqa.framework.helper.ValidationsHelper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Test_MIE_InsertData extends APITestBase {
    String sHost;
    String sEndpoint;

    @BeforeClass
    public void getEnvironment(ITestContext testContext) throws IOException {
        sHost = PropertiesHelper
                .getHostStringFromPropertiesFile(sPropertyFileName, Test_MIE_InsertData.class);
        sEndpoint = "/v2/utilities/Brands/InsertData?brandKey={brandKey}&brandName={brandName}&targetBrandType={targetBrandType}&currencyId={currencyId}&measurementType={measurementType}";
    }



    @Test(dataProvider = "dataProvider", dataProviderClass = TestDataProvider.class)
    public void insertData(Map<Object, Object> testData, ITestContext testContext)
            throws IOException {
        String clientKey = KeywordsHelper
                .replaceKeyword(testData.get("clientkey").toString(), testContext);
        String targetBrandType = KeywordsHelper
                .replaceKeyword(testData.get("targetbrandtype").toString(), testContext);
        String brandKey = KeywordsHelper
                .replaceKeyword(testData.get("brandkey").toString(), testContext);
        String currencyId = KeywordsHelper
                .replaceKeyword(testData.get("currencyid").toString(), testContext);
        String brandName = KeywordsHelper
                .replaceKeyword(testData.get("brandname").toString(), testContext);
        String measurementType = KeywordsHelper
                .replaceKeyword(testData.get("measurementtype").toString(), testContext);
        String fileName = "MTA_Demo_Retail.csv";
        String responseCode = KeywordsHelper
                .replaceKeyword(testData.get("responsecode").toString(), testContext);
        String successResponse = KeywordsHelper
                .replaceKeyword(testData.get("successresponse").toString(), testContext);
        String errorResponse = KeywordsHelper
                .replaceKeyword(testData.get("errorresponse").toString(), testContext);



        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("ClientKey", clientKey);
        headerMap.put("Token", getSuiteContextValue("authorizationtoken_mie", testContext));

        Map<String, String> pathParameterMap = new HashMap<>();
        pathParameterMap.put("targetBrandType", targetBrandType);
        pathParameterMap.put("brandKey", brandKey);
        pathParameterMap.put("currencyId", currencyId);
        pathParameterMap.put("brandName", brandName);
        pathParameterMap.put("measurementType", measurementType);

        RequestSpecification request = RestAssured.given().headers(headerMap).pathParams(pathParameterMap);

        File file = new File(fileName);
        String filePath;
        if (file.isAbsolute()) {
            filePath = fileName;
        } else{
            filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "inputfiles" + File.separator + "mie"+ File.separator + fileName;
        }
        File inputFile = new File(filePath);

        logInfo("Location of File:" + inputFile);

        Response response = request.given().multiPart("fileName[0][]", inputFile).post(sHost+sEndpoint);

        logInfo("Endpoint URL: " +sHost+sEndpoint);
        logResponse(response);

        boolean isResponseCodeCorrect = (response.getStatusCode() == Integer.parseInt(responseCode));
        assertTrue(isResponseCodeCorrect, "API response status code is  expected. \nExpected : " + Integer.parseInt(responseCode)
                        + "\nObserved : " + response.getStatusCode(),
                "API response status code is not as expected.\nExpected : " + Integer.parseInt(responseCode)
                        + "\nObserved : " + response.getStatusCode());

        String sJSON = response.body().asString();
        if (String.valueOf(response.getStatusCode()).startsWith("2") && responseCode.startsWith("2")) {
            DocumentContext documentContext = JsonPath.parse(sJSON);
            String[] validationNodes = successResponse.split("~");
            for (int i = 0; i < validationNodes.length; i++) {
                String sValidationNode = validationNodes[i];
                String[] nodeValuePairs = sValidationNode.split(":");
                String sNode = nodeValuePairs[0];
                String sValue = nodeValuePairs[1];
                if (sValue.startsWith("[")) {
                    List<String> listOfExpectedValues = getStringAsList(sValue);
                    List<String> listOfObservedValues = documentContext.read(sNode);
                    boolean isExpectedListOfValuesPresent = listOfObservedValues.containsAll(listOfExpectedValues);
                    assertTrue(isExpectedListOfValuesPresent, "SuccessResponse matched with ObservedResponse \nQueryKey : " + sNode + "\nExpectedValue : " + listOfExpectedValues + "\nObservedValue: " + listOfObservedValues, "SuccessResponse not matched with ObservedResponse \nQueryKey : " + sNode + "\nExpectedValue : " + listOfExpectedValues + "\nObservedValue : " + listOfObservedValues);
                } else {
                    String sExpectedValue = sValue;
                    if (sExpectedValue.trim().equalsIgnoreCase("null") || documentContext.read(sNode) == null) {
                        boolean isNull = sExpectedValue.equalsIgnoreCase(documentContext.read(sNode)==null?"null":documentContext.read(sNode).toString());
                        assertTrue(isNull, "SuccessResponse matched with ObservedResponse \nQueryKey : " + sNode + "\nExpectedValue : " + sExpectedValue + "\nObservedValue: " + documentContext.read(sNode), "SuccessResponse not matched with ObservedResponse \nQueryKey : " + sNode + "\nExpectedValue : " + sExpectedValue + "\nObservedValue : " + documentContext.read(sNode));
                    } else {
                        String sObservedValue = documentContext.read(sNode).toString();
                        boolean isExpectedValuePresent = sExpectedValue.equalsIgnoreCase(sObservedValue);
                        assertTrue(isExpectedValuePresent, "SuccessResponse matched with ObservedResponse \nQueryKey : " + sNode + "\nExpectedValue : " + sExpectedValue + "\nObservedValue : " + sObservedValue, "SuccessResponse not matched with ObservedResponse \nQueryKey : " + sNode + "\nExpectedValue : " + sExpectedValue + "\nObservedValue : " + sObservedValue);
                    }
                }
            }
        }

        if (!(errorResponse.trim().equals(""))) {
            String partsKey = "";
            String partsValue = "";
            String observedMessage = "";
            String expectedMessage = "";
            io.restassured.path.json.JsonPath jsonpath = response.jsonPath();
            if(errorResponse.contains(":")) {
                partsKey = errorResponse.substring(0, errorResponse.indexOf(":"));
                partsValue = errorResponse.substring(errorResponse.indexOf(":") + 1);
                observedMessage = jsonpath.get(partsKey);
                expectedMessage = partsValue;
            }
            else{
                observedMessage = response.prettyPrint();
                expectedMessage = errorResponse;
            }
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

    private List<String> getStringAsList (String sExpectedValues){
        String[] sValues = sExpectedValues.replace("[", "").replace("]", "").split(",");
        return Arrays.asList(sValues);
    }

}



