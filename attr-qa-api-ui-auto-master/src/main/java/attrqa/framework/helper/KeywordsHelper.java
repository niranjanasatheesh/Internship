package attrqa.framework.helper;

import attrqa.framework.constants.Constants;
import attrqa.framework.reporting.ExtentTestManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.testng.ITestContext;

/**
 * utility class to translate framework keywords
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class KeywordsHelper {

    public static String removeSquareBrackets(String sInputString) {
        String extractedVal = sInputString.replace("[", "").replace("]", "");
        return extractedVal;
    }

    public static String replaceKeyword(String sInputString, ITestContext testContext)
            throws IOException {
        //replace | with $ for dealing with regex in replaceAll
        Map<String, String> keywordMap = new HashMap<>();
        String sPrefix = Constants.KW_PREFIX;
        String sSuffix = Constants.KW_SUFFIX;
        String[] keywords = StringUtils.substringsBetween(sInputString, sPrefix, sSuffix);
        sInputString = sInputString.replaceAll("\\|", ":");
        String sProcessedString = sInputString;
        if (keywords != null) {
            for (int i = 0; i < keywords.length; i++) {
                String sKeyword = keywords[i];
                String sProcessedKeyword = sKeyword.replaceAll("\\|", ":");
                String sProcessedPhraseToReplace = sPrefix + sProcessedKeyword + sSuffix;
                String sPhraseToSubstitute = "";
                if (sKeyword.startsWith("randomint")) {
                    sPhraseToSubstitute = getRandomNumber(sKeyword);
                } else if (sKeyword.startsWith("randomstring")) {
                    sPhraseToSubstitute = getRandomString(sKeyword);
                    if(sPhraseToSubstitute.contains("#")){
                        String sPhrase = sPhraseToSubstitute.split("#")[0];
                        String sSuiteContextKey = sPhraseToSubstitute.split("#")[1];
                        testContext.getSuite().setAttribute(sSuiteContextKey,sPhrase);
                        ExtentTest extentTest = ExtentTestManager.getExtentTest();
                        extentTest.log(Status.INFO, "saving " + sPhrase + " against " + sSuiteContextKey + " in suite-context");
                        sPhraseToSubstitute = sPhrase;
                    }
                } else if (sKeyword.startsWith("today")) {
                    sPhraseToSubstitute = getDate(sKeyword);
                } else {
                    if (!(testContext.getSuite().getAttribute(sKeyword) == null)) {
                        sPhraseToSubstitute = testContext.getSuite().getAttribute(sKeyword).toString();
                    } else {
                        throw new RuntimeException("Test context value not found for " + sKeyword);
                    }
                }
                keywordMap.put(sProcessedPhraseToReplace, sPhraseToSubstitute);
            }
        }

        Set<String> keySet = keywordMap.keySet();
        Iterator<String> keySetIterator = keySet.iterator();
        while (keySetIterator.hasNext()) {
            String sKeyword = keySetIterator.next();
            String sKeywordValue = keywordMap.get(sKeyword);
            sProcessedString = sProcessedString.replaceAll(sKeyword, sKeywordValue);
            ExtentTest extentTest = ExtentTestManager.getExtentTest();
            extentTest.log(Status.INFO, "replacing " + sKeyword + " with " + sKeywordValue);
        }
        return sProcessedString;
    }

    private static String getRandomNumber(String sKeyword) {
        int randomNumber;
        if (sKeyword.contains("|")) {
            randomNumber = getRandomNumber(Integer.valueOf(sKeyword.split("\\|")[1]));
        } else {
            randomNumber = getRandomNumber(5);
        }
        return String.valueOf(randomNumber);
    }

    private static int getRandomNumber(int size) {
        String startNum = "1";
        String endNum = "9";
        for (int i = 1; i < size; i++) {
            startNum += "0";
            endNum += "9";
        }
        Random random = new Random();
        return Integer.valueOf(startNum) + random.nextInt(Integer.valueOf(endNum));
    }


    /**
     * Edited by surajv.bhattathiri to bring in suite-context storage
     * @param sKeyword
     * @return
     */

    public static String getRandomString(String sKeyword){
        String randomString;
        if (sKeyword.contains("|")) {
            Map<String, String> keywordAttributesMap = new HashMap<>();
            String sKeywordAttributes = sKeyword.split("\\|")[1];
            String[] attributes = sKeywordAttributes.split(",");
            for(String sAttribute : attributes){
                String sAttributeKeyValue = sAttribute.trim();
                String[] keyValue = sAttributeKeyValue.split("=");
                keywordAttributesMap.put(keyValue[0].trim(),keyValue[1].trim());
            }
            if(keywordAttributesMap.containsKey("prefix")){
                randomString = keywordAttributesMap.get("prefix")+getRandomString(10);
            }
            else{
                randomString = getRandomString(10);
            }
            if(keywordAttributesMap.containsKey("key")){
                randomString = randomString+"#"+keywordAttributesMap.get("key");
            }
        } else {
            randomString = getRandomString(10);
        }
        return randomString;
    }



    private static String getRandomString(int length) {
        return generateRandomString(length, true, false);
    }

    private static String generateRandomString(int length, boolean useLetters, boolean useNumbers) {
        String randomString = RandomStringUtils.random(length, useLetters, useNumbers);
        return randomString;
    }

    /**
     * accepts keyword in following formats today|<date_format> , today|5|<date_format>,
     * today|-5|<date_format>
     */
    public static String getDate(String sKeyword) {
        String sDate = "";
        DateTime today = DateTime.now();
        DateTime calculatedDate;
        DateTimeFormatter dateTimeFormatter;
        String[] keywordParts = sKeyword.split("\\|");
        if (keywordParts.length == 2) {
            dateTimeFormatter = DateTimeFormat.forPattern(keywordParts[1].trim());
            sDate = today.toString(dateTimeFormatter);
        } else if (keywordParts.length == 3) {
            int offSet = Integer.parseInt(keywordParts[1].trim());
            dateTimeFormatter = DateTimeFormat.forPattern(keywordParts[2].trim());
            calculatedDate = new DateTime(today.plusDays(offSet));
            sDate = calculatedDate.toString(dateTimeFormatter);
        } else {
            throw new RuntimeException("Invalid keyword format for date! " + sKeyword);
        }

        return sDate;
    }

    /**
     * accepts a String representation of array and converts it into a list
     */
    public static List<String> getStringArrayAsList(String sArrayString) {
        List<String> list = new ArrayList<>();
        //removing square-brackets
        sArrayString = removeSquareBrackets(sArrayString);
        String[] arrayValues = sArrayString.split(",");
        for (int i = 0; i < arrayValues.length; i++) {
            //remove space and removing double-quotes
            String processedArrayValue = arrayValues[i].trim().replaceAll("^\"|\"$", "");
            list.add(processedArrayValue);
        }
        return list;
    }


}
