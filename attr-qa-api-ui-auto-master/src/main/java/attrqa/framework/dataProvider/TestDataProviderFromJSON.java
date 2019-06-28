package attrqa.framework.dataProvider;

import attrqa.framework.helper.PropertiesHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

public class TestDataProviderFromJSON {

  @DataProvider(name = "dataProvider")
  public Object[][] getTestData(Method method, ITestContext testContext) throws IOException {
    Object[][] data = null;
    //retrieve sPropertyFileName set in iTestContext.iSuite
    String sPropertyFileName = testContext.getSuite().getAttribute("propertyFileName").toString();
    String sInvokingClassName = method.getDeclaringClass().getSimpleName();
    String sJSONDataFile = PropertiesHelper
        .getJSONTestDataFileNameFromPropertiesFile(sPropertyFileName, sInvokingClassName);

    try {
      data = getDataSet(sJSONDataFile, testContext);
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (data == null) {
      throw new RuntimeException("Data Provider has returned null");
    }

    return data;
  }

  public Object[][] getDataSet(String sJSONDataFile, ITestContext testContext) {
    sJSONDataFile = "SampleTestData.json";
    Object[][] arrayOfDataSetMap = null;

    JSONParser parser = new JSONParser();

    String file_path =
        System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
            + File.separator + "testdatasheet" + File.separator + sJSONDataFile;

    try(FileReader fileReader = new FileReader(file_path)){

      JSONObject jsonObject = (JSONObject)parser.parse(fileReader);
      JSONArray testData =  (JSONArray)jsonObject.get("testdata");

      int totalRows = testData.size();

      arrayOfDataSetMap = new Object[totalRows][1];

      Iterator testDataIterator = testData.iterator();
      int rowNum = 0;
      while(testDataIterator.hasNext()){
        Map<String, String> dataSetMap = new HashMap<>();
        JSONObject testDataRowObject = (JSONObject)testDataIterator.next();
        Iterator<String> testDataColumnTitlesIterator = testDataRowObject.keySet().iterator();
        while(testDataColumnTitlesIterator.hasNext()){
          String sKey = testDataColumnTitlesIterator.next();
          dataSetMap.put(sKey,testDataRowObject.get(sKey).toString());
        }
        arrayOfDataSetMap[rowNum][0] = dataSetMap;
        rowNum++;
      }
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return arrayOfDataSetMap;
  }

}
