package org.vp.databases;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.bson.Document;
import org.vp.scraping.VcModelScraping;
import org.vp.vc.profile.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mrahman on 3/14/17.
 */
public class GoogleSheetIntegration {
    public static VCFields vcFields = new VCFields();
    /** Application name. */
    private static final String APPLICATION_NAME =
            "Google Sheets API Java VcModelScraping";
    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                VcModelScraping.class.getResourceAsStream("/service.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = null;
        try {
            credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

            System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return credential;
    }

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    public static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public VCProfile getGoogleData() throws IOException {
        VCProfile vcProfile = null;
        List<VCProfile> vcProfileList = new ArrayList<>();
        FundingHistory fundingHistory = new FundingHistory();
        List<FundingHistory> fh = new ArrayList<FundingHistory>();
        List<String> categoriesList = null;
        Sheets service = getSheetsService();
        String spreadsheetId = "1l_JyK4GxZBLZG8GGIJiuT0G3oapn33CQih5vYubgwo0";
        String range = "First Round Capital";
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.size() == 0) {
            System.out.println("No data found.");
        } else {
            for (List row : values) {

                String date = (String) row.get(0).toString();
                String companyName = (String) row.get(1).toString();
                String fundingAmount = (String) row.get(2).toString();
                String fundingRound = (String) row.get(3).toString();
                String categories = (String) row.get(4).toString();
                String[] categoriesArray = categories.split(",");
                categoriesList = new ArrayList<String>(Arrays.asList(categoriesArray));
                fundingHistory.setFundingDate(date);
                fundingHistory.setCompanyName(companyName);
                fundingHistory.setFundingAmount(fundingAmount);
                fundingHistory.setFundingRound(fundingRound);
                fundingHistory.setCategories(categoriesList);
                fundingHistory = new FundingHistory(fundingHistory.getFundingDate(), fundingHistory.getCompanyName(),
                        fundingHistory.getFundingAmount(), fundingHistory.getFundingRound(), fundingHistory.getCategories());
                fh.add(fundingHistory);
                vcProfile = new VCProfile(fh);
                fundingHistory = new FundingHistory();
                //System.out.printf("%s, %s,%s, %s,%s,\n", row.get(0), row.get(1), row.get(2), row.get(3),row.get(4));

            }
        }
        return vcProfile;
    }
}
