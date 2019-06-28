package attrqa.maestro.Mar19Release.ui;

import attrqa.framework.base.UITestBase;
import attrqa.framework.pageobjects.common.NLSNAnswersLoginPage;
import attrqa.framework.pageobjects.factory.PageInstanceFactory;
import attrqa.framework.pageobjects.maestro.MarketingIntelligenceAdminPage;
import attrqa.framework.pageobjects.maestro.RefreshConfigurationPageElement;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.Test;

public class TestDummy extends UITestBase {

  @Test
  public void doTest() throws Exception{
    logInfo("in test method");

    pageInstanceFactory = new PageInstanceFactory(driverThreadLocal.get());

    NLSNAnswersLoginPage nlsnAnswersLogin = pageInstanceFactory.getPageInstance(
        NLSNAnswersLoginPage.class);

    nlsnAnswersLogin.launch("https://develop-admin.nielsen-attr.com/");
    nlsnAnswersLogin.doLogin("sreekumar.mohan@nielsen.com","Welcome@4iq", false);

    MarketingIntelligenceAdminPage marketingIntelligenceAdminPage = pageInstanceFactory.
        getPageInstance(MarketingIntelligenceAdminPage.class);

    marketingIntelligenceAdminPage.expandConfigurationMenu();

    marketingIntelligenceAdminPage.clickRefreshMenu();

    RefreshConfigurationPageElement refreshConfiguration = pageInstanceFactory.
        getPageInstance(RefreshConfigurationPageElement.class);

    refreshConfiguration.setRefreshCadence("WEEKLY");

    TimeUnit.SECONDS.sleep(1000);

  }

}
