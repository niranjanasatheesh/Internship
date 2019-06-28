package attrqa.framework.dataProvider;

import attrqa.framework.helper.GoogleSheetsHelper;
import attrqa.framework.helper.PropertiesHelper;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

/**
 * test data provider
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class TestDataProvider {

  @DataProvider(name = "dataProvider")
  public Object[][] getTestData(Method method, ITestContext testContext) throws IOException {
    Object[][] data = null;
    //retrieve sPropertyFileName set in iTestContext.iSuite
    String sPropertyFileName = testContext.getSuite().getAttribute("propertyFileName").toString();
    String sInvokingClassName = method.getDeclaringClass().getSimpleName();
    String sWorkbook = PropertiesHelper
        .getTestDataFileNameFromPropertiesFile(sPropertyFileName, sInvokingClassName);
    String sSheet = PropertiesHelper
        .getTestDataSheetNameFromPropertiesFile(sPropertyFileName, sInvokingClassName);

    try {
      data = getDataSet(sWorkbook, sSheet, testContext);
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (data == null) {
      throw new RuntimeException("Data Provider has returned null");
    }

    return data;
  }

  public Object[][] getDataSet(String sWorkbook, String sSheet, ITestContext testContext)
      throws Exception {
    Object dataSet[][] = null;

    XSSFWorkbook workBook;
    XSSFSheet sheet;

    if (sWorkbook.contains(".xls")) {
      Path path = Paths.get(sWorkbook);
      if (path.isAbsolute()) {
        workBook = new XSSFWorkbook(sWorkbook);
      } else {
        String file_path =
            System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                + File.separator + "testdatasheet" + File.separator + sWorkbook;
        workBook = new XSSFWorkbook(new File(file_path));
      }
      sheet = workBook.getSheet(sSheet);
      dataSet = constructArrayOfMaps(sheet, testContext);
    } else {
      List<List<Object>> values = GoogleSheetsHelper.getGoogleSheetsData(sWorkbook, sSheet);
      dataSet = constructArrayOfMaps(values, testContext);
    }

    return dataSet;
  }


  public int getRowCount(XSSFSheet sheet) {
    int rowCount;
    rowCount = sheet.getPhysicalNumberOfRows();
    return rowCount;
  }


  public int getColumnCount(XSSFSheet sheet) {
    int colCount;
    colCount = sheet.getRow(0).getPhysicalNumberOfCells();
    return colCount;
  }

  public Object[][] constructArrayOfMaps(XSSFSheet sheet, ITestContext testContext)
      throws Exception {
    int totalRows = getRowCount(sheet);
    int totalCols = getColumnCount(sheet);

    Object[][] arrayOfDataSetMap = new Object[totalRows - 1][1];

    DataFormatter dataFormatter = new DataFormatter();

    String sData;
    String sColumnHeader;
    int dataRowIndex = 0;
    for (int rowNum = 1; rowNum < totalRows; rowNum++, dataRowIndex++) {
      Map<String, String> dataSetMap = new HashMap<>();
      for (int colNum = 0; colNum < totalCols; colNum++) {
        sColumnHeader = sheet.getRow(0).getCell(colNum).getStringCellValue();
        XSSFCell xssfCell = sheet.getRow(rowNum).getCell(colNum);
        sData = dataFormatter.formatCellValue(xssfCell);
        dataSetMap.put(sColumnHeader.toLowerCase(), sData);
      }
      arrayOfDataSetMap[rowNum - 1][0] = dataSetMap;
    }

    return arrayOfDataSetMap;
  }

  public Object[][] constructArrayOfMaps(List<List<Object>> values, ITestContext testContext)
      throws Exception {
    int totalRows = values.size() - 1; //excluding header row
    int totalCols = 0;
    for (int i = 0; i < values.size(); i++) {
      int noOfCols = values.get(i).size();
      if (noOfCols > totalCols) {
        totalCols = noOfCols;
      }
    }

    Object[][] arrayOfDataSetMap = new Object[totalRows][1];

    if (!(values == null || values.isEmpty())) {
      String[] columnHeaders = new String[totalCols];
      int rowNum = 0;
      for (List row : values) {
        if (rowNum == 0) {
          if (row.size() < totalCols) {
            throw new RuntimeException("Column header missing in the data-sheet!");
          }
          for (int j = 0; j < totalCols; j++) {
            columnHeaders[j] = row.get(j).toString();
          }
        } else {
          Map<Object, Object> dataSetMap = new LinkedHashMap<>();
          for (int j = 0; j < row.size(); j++) {
            String sCellValue = row.get(j).toString();
            dataSetMap.put(columnHeaders[j].toLowerCase(), sCellValue);
          }
          // if the row has null values at the end, need to
          // process those nulls and convert them to empty cells
          // to prevent issues later when traversing the data-set
          for (int j = row.size(); j < totalCols; j++) {
            dataSetMap.put(columnHeaders[j].toLowerCase(), "");
          }

          arrayOfDataSetMap[rowNum - 1][0] = dataSetMap; //setting arrayOfDataSetMap
        }
        rowNum++;
      }
    } else {
      System.out.println("No test data found!");
    }
    return arrayOfDataSetMap;
  }

}
