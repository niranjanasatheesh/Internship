package attrqa.optimusprime.Mar19Release.api;

import attrqa.framework.base.APITestBase;
import attrqa.framework.dataProvider.TestDataProvider;
import attrqa.framework.helper.KeywordsHelper;
import attrqa.framework.helper.PropertiesHelper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static attrqa.framework.helper.AWSHelper.queryAthena;

public class Test_Media_FlightChart extends APITestBase {
  String sHost;
  private String sEndPoint;

  @BeforeClass
  public void getEnvironment(ITestContext testContext)
          throws IOException {
    sHost = PropertiesHelper.getHostStringFromPropertiesFile(sPropertyFileName, Test_Media_FlightChart.class);
    sEndPoint = "scenario-runs/latest/mediaplans";
  }

  @Test(dataProvider = "dataProvider", dataProviderClass = TestDataProvider.class)
  public void getDataFromFlightChart(Map<Object, Object> testData, ITestContext testContext)
          throws Exception {

    String authorizationKey = KeywordsHelper
            .replaceKeyword(testData.get("authorizationkey").toString(), testContext);
    String brandKey = KeywordsHelper
            .replaceKeyword(testData.get("brandkey").toString(), testContext);
    String clientKey = KeywordsHelper
            .replaceKeyword(testData.get("clientkey").toString(), testContext);
    String responseCode = KeywordsHelper
            .replaceKeyword(testData.get("statuscode").toString(), testContext);
    String spendIncrement = KeywordsHelper
            .replaceKeyword(testData.get("spendincrement").toString(), testContext);
    String columnTitle = KeywordsHelper
            .replaceKeyword(testData.get("metacolumntitle").toString(), testContext);
    String groupBy = KeywordsHelper
            .replaceKeyword(testData.get("groupby").toString(), testContext);
    String view = KeywordsHelper
            .replaceKeyword(testData.get("view").toString(), testContext);
    String multiplier = KeywordsHelper
            .replaceKeyword(testData.get("multiplier").toString(), testContext);
    String rank = KeywordsHelper
            .replaceKeyword(testData.get("rank").toString(), testContext);

    Map<String, String> headersMap = new HashMap<String, String>();
    headersMap.put("Authorization", authorizationKey);

    Map<String, String> queryParamsMap = new HashMap<>();
    queryParamsMap.put("brandKey", brandKey);
    queryParamsMap.put("spendIncrement", spendIncrement);
    queryParamsMap.put("groupBy", groupBy);
    queryParamsMap.put("view", view);
    queryParamsMap.put("clientKey", clientKey);

    RequestSpecification request = RestAssured.given().headers(headersMap).queryParams(queryParamsMap);
    Response response = request.get(sHost + sEndPoint);
    logInfo("endpoint: " + sEndPoint);
    logResponse(response);

    //RESPONSE CODE
    boolean isResponseCodeCorrect = (response.getStatusCode() == Integer.parseInt(responseCode));
    assertTrue(isResponseCodeCorrect, "API response status code is as expected",
            "API response status code is not as expected. Expected : " + Integer.parseInt(responseCode)
                    + " but Observed : " + response.getStatusCode());
    if(response.getStatusCode()==200) {

      String sJSON = response.body().asString();
      DocumentContext documentContext = JsonPath.parse(sJSON);
      String query = "select ds_week,sum(spend) spend, sum(optspend) optspend,sum(mrev) mrev, sum(optmrev) optmrev,sum(dueto_vol) dueto_vol, sum(optdueto_vol) optduetovol,sum(exposure) exposure, sum(optexposure) optexposure,sum(profit) profit, sum(optprofit) optprofit from dev_fancyf3.rpt_decomps_opt_forward where scenario_run_id=100001 and multiplier={1} group by model_period, ds_week order by model_period asc, ds_week asc";
      String sql = query;
      sql = sql.replace("{1}", multiplier);
      final List<String> list = queryAthena("us-east-1", "dev_fancyf3", "s3://attr-nonprod2-useast1/EV/FANCYF3/FANCYF3/OPTIM-OUT/automation_files/", sql);

      //META-COLUMNS-TITLES
      List<String> expectedColumnTitle = new ArrayList<String>();
      expectedColumnTitle.addAll(Arrays.asList(columnTitle.split(",")));
      String sTitle = "meta.columns[*].title";
      List<String> observedColumnTitle = new ArrayList<>(documentContext.read(sTitle));
      boolean metacolumntitleequal = expectedColumnTitle.equals(observedColumnTitle);
      assertTrue(metacolumntitleequal, " meta.columns.title.title is as expected",
              "meta.columns.title is not as expected. Expected : " + expectedColumnTitle
                      + " but Observed : " + observedColumnTitle);


      //META-RANK
//    List<Integer> expectedRank = new ArrayList<>();
//    for (String s : rank.split(",")) {
//      expectedRank.add(Integer.valueOf(s));
//    }
      List<Integer> expectedRank = Arrays.stream(rank.split(","))
              .map(s -> Integer.valueOf(s))
              .collect(Collectors.toList());
      String sRank = "meta.columns[*].rank";
      List<Integer> observedRank = new ArrayList<>(documentContext.read(sRank));
      boolean rankEqual = expectedRank.equals(observedRank);
      assertTrue(rankEqual, " meta.columns.rank is as expected",
              "meta.columns.rank is not as expected. Expected : " + expectedRank
                      + " but Observed : " + observedRank);


      //DATA-DATE
      String sDate = "data[0].seriesData[0].date";
      String observedDate = (documentContext.read(sDate));
      String expectedDate = list.get(11);
      boolean dateEqual = expectedDate.equals(observedDate);
      assertTrue(dateEqual, " data.seriesdata.date is as expected",
              "data.seriesdata.date is not as expected. Expected : " + expectedDate
                      + " but Observed : " + observedDate);
      //DATA-SPEND
      String sspend = "data[0].seriesData[0].spend";
      String observedSpend = documentContext.read(sspend).toString();
      String expectedSpend = list.get(13);
      boolean spendEqual = (expectedSpend.equalsIgnoreCase(observedSpend));
      assertTrue(spendEqual, " data.seriesdata.spend is as expected",
              "data.seriesdata.spend is not as expected. Expected : " + expectedSpend
                      + " but Observed : " + observedSpend);

      //DATA-TOUCHPOINTS
      String stouchpoint = "data[0].seriesData[0].touchpoints";
      String observedTouchpoint = documentContext.read(stouchpoint).toString();

      String expectedTouchpoint = (list.get(19));
      boolean touchpointEqual = expectedTouchpoint.equals(observedTouchpoint);
      assertTrue(touchpointEqual, " data.seriesdata.touchpoints is as expected",
              "data.seriesdata.touchpoints is not as expected. Expected : " + expectedTouchpoint
                      + " but Observed : " + observedTouchpoint);

      //DATA-DUETOVOL
      String sduetovol = "data[0].seriesData[0].duetovol";
      String observedDuetoVol = documentContext.read(sduetovol).toString();
      String expectedDuetoVol = (list.get(17));
      boolean duetoVolEqual = expectedDuetoVol.equals(observedDuetoVol);
      assertTrue(duetoVolEqual, " data.seriesdata.duetovol is as expected",
              "data.seriesdata.duetovol is not as expected. Expected : " + expectedDuetoVol
                      + " but Observed : " + observedDuetoVol);

      //DATA-PROFIT
      String sprofit = "data[0].seriesData[0].profit";
      String observedProfit = (documentContext.read(sprofit)).toString();

      String expectedProfit = (list.get(21));
      boolean profitEqual = expectedProfit.equals(observedProfit);
      assertTrue(profitEqual, " data.seriesdata.profit is as expected",
              "data.seriesdata.profit is not as expected. Expected : " + expectedProfit
                      + " but Observed : " + observedProfit);

      //DATA-MREV
      String smrev = "data[0].seriesData[0].mrev";
      String observedmrev = (documentContext.read(smrev)).toString();
      String expectedmrev = (list.get(15));
      boolean mrevEqual = expectedmrev.equals(observedmrev);
      assertTrue(mrevEqual, " data.seriesdata.mrev is as expected",
              "data.seriesdata.mrev is not as expected. Expected : " + expectedmrev
                      + " but Observed : " + observedmrev);

      //DATA-ROI
      String sroi = "data[0].seriesData[0].roi";
      String observedroi = (documentContext.read(sroi)).toString();
      double profit = Double.parseDouble(list.get(21));
      double spend = Double.parseDouble(list.get(13));
      double roi = profit / spend;

      String expectedroi = String.valueOf(roi);
      boolean roiEqual = expectedroi.equals(observedroi);
      assertTrue(roiEqual, " data.seriesdata.roi is as expected",
              "data.seriesdata.roi is not as expected. Expected : " + expectedroi
                      + " but Observed : " + observedroi);
      //DATA-EFFICIENCY
      String mefficiency = "data[0].seriesData[0].efficiency";
      String observedefficiency = (documentContext.read(mefficiency)).toString();
      double duetovol = Double.parseDouble(list.get(17));
      double efficiency = duetovol / spend;
      String expectedefficiency = String.valueOf(efficiency);
      boolean efficiencyEqual = expectedefficiency.equals(observedefficiency);
      assertTrue(efficiencyEqual, " data.seriesdata.efficiency is as expected",
              "data.seriesdata.efficicency is not as expected. Expected : " + expectedefficiency
                      + " but Observed : " + observedefficiency);


      //DATA-ROAS
      String mroas = "data[0].seriesData[0].roas";
      String observedroas = (documentContext.read(mroas)).toString();
      double mrev = Double.parseDouble(list.get(15));

      double roas = mrev / spend;
      String expectedroas = String.valueOf(roas);
      boolean roasEqual = expectedroas.equals(observedroas);
      assertTrue(roasEqual, " data.seriesdata.roas is as expected",
              "data.seriesdata.roas is not as expected. Expected : " + expectedroas
                      + " but Observed : " + observedroas);

      //DATA-EFFECTIVENESS
      String meffectiveness = "data[0].seriesData[0].effectiveness";
      String observedEffectiveness = ((documentContext.read(meffectiveness))).toString();
      double touchpoint = Double.parseDouble(list.get(19));


      double effectiveness = duetovol / (touchpoint / 1000);
      String expectedeffectiveness = String.valueOf(effectiveness);
      boolean effectivenessEqual = expectedeffectiveness.equals(observedEffectiveness);
      assertTrue(effectivenessEqual, " data.seriesdata.effectiveness is as expected",
              "data.seriesdata.effectiveness is not as expected. Expected : " + expectedeffectiveness
                      + " but Observed : " + observedEffectiveness);

    }

  }
}