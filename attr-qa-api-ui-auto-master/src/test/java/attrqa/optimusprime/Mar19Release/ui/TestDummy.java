package attrqa.optimusprime.Mar19Release.ui;

import attrqa.framework.base.UITestBase;
import attrqa.framework.pageobjects.common.NLSNAnswersLoginPage;
import attrqa.framework.pageobjects.factory.PageInstanceFactory;
import attrqa.framework.pageobjects.optimusprime.AttributionOptimizationToolPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestDummy extends UITestBase {

  @BeforeClass
  public void beforeClass(){
    System.out.println("in before class");

  }

  @Test
  public void doTest(){
    logInfo("in test method");

    pageInstanceFactory = new PageInstanceFactory(driverThreadLocal.get());

    NLSNAnswersLoginPage nlsnAnswersLogin = pageInstanceFactory.getPageInstance(
        NLSNAnswersLoginPage.class);

    nlsnAnswersLogin.launch("https://dev-optimize.nielsen-attr.com/");


    logInfo(nlsnAnswersLogin.getPageHeader());
    nlsnAnswersLogin.doLogin("sreekumar.mohan@nielsen.com","Welcome@4iq", false);

    AttributionOptimizationToolPage attributionOptimizationTool = pageInstanceFactory.
        getPageInstance(AttributionOptimizationToolPage.class);
    logInfo(attributionOptimizationTool.getSectionHeaderLabelText());
    String sAuthToken = attributionOptimizationTool.getAuthorizationToken();
    System.out.println(sAuthToken);

  }

}
