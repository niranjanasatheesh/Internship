package attrqa.framework.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentKlovReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * extent report related stuff
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class ExtentReportManager {

  private static ExtentReports extentReports;

  /**
   * This method configures the appearance of the extent report
   */
  public synchronized static ExtentReports createReportInstance() {
    String reportFileName =
        new SimpleDateFormat("hh-mm-ss dd-MM-yyyy").format(new Date()) + "reports.html";
    String reportFilePath =
        System.getProperty("user.dir") + File.separator + "reports" + File.separator
            + reportFileName;
    ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportFilePath);
    htmlReporter.config().setTheme(Theme.DARK); //Theme.STANDARD
    htmlReporter.config().setDocumentTitle("automation run report");
    htmlReporter.config().setEncoding("utf-8");
    htmlReporter.config().setReportName("");

    ExtentKlovReporter klovReporter = new ExtentKlovReporter();
    klovReporter.initMongoDbConnection("localhost", 27017);
    klovReporter.setProjectName("project name");
    klovReporter.setReportName(reportFileName);
    klovReporter.initKlovServerConnection("http://localhost");

    extentReports = new ExtentReports();
    extentReports.attachReporter(htmlReporter);
    extentReports.attachReporter(klovReporter);
    return extentReports;
  }

  public synchronized static ExtentReports createReportInstance(String suiteName) {
    Date date = new Date();
    String reportFileName = new SimpleDateFormat("dd-MM-yyyy").format(date) + "_" + suiteName + "_"
        + new SimpleDateFormat("hh-mm-ss").format(date) + "_run_report.html";
    String reportFilePath =
        System.getProperty("user.dir") + File.separator + "reports" + File.separator
            + reportFileName;



    try {
      File file1 = new File("E:\\Niranjana\\New Folder\\filename.txt");
      file1.createNewFile();
      FileWriter fw1 = new FileWriter(file1);
      BufferedWriter bw1 = new BufferedWriter(fw1);
      bw1.write(reportFileName);
      bw1.flush();
      bw1.close();
    }
    catch(Exception e ){}


    ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportFilePath);
    htmlReporter.config().setTheme(Theme.DARK); //Theme.STANDARD
    htmlReporter.config().setDocumentTitle("automation run report");
    htmlReporter.config().setEncoding("utf-8");
    htmlReporter.config().setReportName("");

    extentReports = new ExtentReports();
    extentReports.attachReporter(htmlReporter);
    return extentReports;
  }
}
