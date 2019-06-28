package attrqa.framework.listener;

import attrqa.framework.base.APITestBase;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * test-ng listener
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class Listener extends APITestBase implements ITestListener {

  @Override
  public void onTestStart(ITestResult iTestResult) {

  }

  @Override
  public void onTestSuccess(ITestResult iTestResult) {

  }

  @Override
  public void onTestFailure(ITestResult iTestResult) {
    if (iTestResult.getThrowable().toString()
        .contains("com.google.api.client.googleapis.json.GoogleJsonResponseException")) {
      assertTrue(false, "", "possibly wrong sheet name configured in properties file");
    }

  }

  @Override
  public void onTestSkipped(ITestResult iTestResult) {

  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

  }

  @Override
  public void onStart(ITestContext iTestContext) {

  }

  @Override
  public void onFinish(ITestContext iTestContext) {

  }
}
