package attrqa.framework.helper;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * utility class to read data from google-sheets
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class GoogleSheetsHelper {

  private static final String APPLICATION_NAME = "Google Sheets API ";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final List<String> SCOPES = Collections
      .singletonList(SheetsScopes.SPREADSHEETS_READONLY);
  private static final String CREDENTIALS_FILE =
      System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
          + File.separator + "resources" + File.separator + "googlesheetscreds.json";
  private static final String TOKENS_DIRECTORY = "google_tokens";

  public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
      throws IOException {
    InputStream inputStream = new FileInputStream(CREDENTIALS_FILE);
    GoogleClientSecrets clientSecrets = GoogleClientSecrets
        .load(JSON_FACTORY, new InputStreamReader(inputStream));
    File tokensDirectory = new File(TOKENS_DIRECTORY);
    FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(tokensDirectory);
    GoogleAuthorizationCodeFlow authorizationCodeFlow =
        new GoogleAuthorizationCodeFlow
            .Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(fileDataStoreFactory)
            .setAccessType("offline").build();
    AuthorizationCodeInstalledApp authorizationCodeInstalledApp = new AuthorizationCodeInstalledApp(
        authorizationCodeFlow, new LocalServerReceiver());
    Credential credential = authorizationCodeInstalledApp.authorize("user");
    return credential;
  }

  public static Map<String, List<List<Object>>> getGoogleSheetsData(String sTestCasesFilePath)
      throws GeneralSecurityException, IOException {
    Map<String, List<List<Object>>> dataMap = new LinkedHashMap<>();
    NetHttpTransport HTTP_TRANSPORT = null;
    HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    Sheets sheetsAPIService = new Sheets
        .Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME).build();
    Spreadsheet googleSheet = sheetsAPIService
        .spreadsheets()
        .get(sTestCasesFilePath)
        .setIncludeGridData(false)
        .execute();

    int noOfSheets = googleSheet.getSheets().size();

    for (int i = 0; i < noOfSheets; i++) {
      String sSheetName = googleSheet.getSheets().get(i).getProperties().getTitle();
      ValueRange valueRange = sheetsAPIService
          .spreadsheets()
          .values()
          .get(sTestCasesFilePath, sSheetName)
          .execute();
      List<List<Object>> values = valueRange.getValues();
      dataMap.put(sSheetName, values);
    }

    return dataMap;
  }


  public static List<List<Object>> getGoogleSheetsData(String sTestCasesFilePath,
      String sTestCasesSheetName) throws GeneralSecurityException, IOException {
    NetHttpTransport HTTP_TRANSPORT = null;
    HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    Sheets sheetsAPIService = new Sheets
        .Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME).build();
    ValueRange valueRange = sheetsAPIService
        .spreadsheets()
        .values()
        .get(sTestCasesFilePath, sTestCasesSheetName)
        .execute();

    return valueRange.getValues();
  }
}
