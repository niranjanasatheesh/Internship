package attrqa.framework.pageobjects.factory;

import attrqa.framework.pageobjects.base.BasePage;
import attrqa.framework.reporting.ExtentTestManager;
import attrqa.framework.testdriver.driver.Driver;

/**
 * factory to generate instances of page objects
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class PageInstanceFactory {

    protected Driver driver;

    public PageInstanceFactory(Driver driver){
        this.driver = driver;
    }

    public  <TPage extends BasePage> TPage getPageInstance (Class<TPage> pageClass) {
        try {
            ExtentTestManager.getExtentTest().info("initializing "+pageClass.getName());
            return pageClass.getDeclaredConstructor(Driver.class).newInstance(this.driver);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
