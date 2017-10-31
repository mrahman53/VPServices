package org.vp.scraping;

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
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.vp.databases.ConnectMongo;
import org.vp.databases.VCFields;
import org.vp.vc.profile.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mrahman on 8/5/17.
 */
public class FHScraping {
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

    public static String spreadsheetId = "1_948_WqSAulTKzoC_aYZA5B0Qj0ne1srv1ktTBBljCA";
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
        InputStream in = VcModelScraping.class.getResourceAsStream("/client_secret.json");
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
    public static List<String> getVcListFromSheet() throws IOException {
        List<String> vcNameList = new ArrayList<>();
        Sheets service = getSheetsService();
        String vc = "VcToBeScrape";
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, vc)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.size() == 0) {
            System.out.println("No data found.");
        } else {
            for (List row : values) {
                try {
                    String vcName = (String) row.get(0).toString();
                    vcNameList.add(vcName);
                } catch (Exception ex) {

                }
            }
        }
        return vcNameList;
    }

    public static void main(String[] args) throws IOException {
        List<String> vcList = getVcListFromSheet();
        for(int i=1; i<vcList.size(); i++) {
            ConnectMongo connectMongo = new ConnectMongo();
            FundingHistory fundingHistory = new FundingHistory();
            List<FundingHistory> fh = new ArrayList<FundingHistory>();
            List<FundingHistory> fhSorted = new ArrayList<FundingHistory>();
            Sheets service = getSheetsService();
            String vc = vcList.get(i);
            ValueRange response = service.spreadsheets().values()
                    .get(spreadsheetId, vc)
                    .execute();
            List<List<Object>> values = response.getValues();
            if (values == null || values.size() == 0) {
                System.out.println("No data found.");
            } else {
                for (List row : values) {
                    try {
                        String date = (String) row.get(0).toString();
                        String companyName = (String) row.get(1).toString();
                        String fundingAmount = (String) row.get(2).toString();
                        String fundingRound = (String) row.get(3).toString();

                    //String categories = (String) row.get(4).toString();
                    //String[] categoriesArray = categories.split(",");
                    //List<String> categoriesList = new ArrayList<String>(Arrays.asList(categoriesArray));
                    fundingHistory.setFundingDate(date);
                    fundingHistory.setCompanyName(companyName);
                    fundingHistory.setFundingAmount(fundingAmount);
                    fundingHistory.setFundingRound(fundingRound);
                    //fundingHistory.setCategories(categoriesList);

                    fundingHistory = new FundingHistory(fundingHistory.getFundingDate(), fundingHistory.getCompanyName(),
                            fundingHistory.getFundingAmount(), fundingHistory.getFundingRound());
                    fh.add(fundingHistory);
                    fundingHistory = new FundingHistory();
                    }catch(Exception ex){
                        System.out.println(values.size());
                    }

                }
            }
            try {
                for (int entry = fh.size() - 1; entry >= 1; entry--) {
                    fhSorted.add(fh.get(entry));
                }
                MongoClient mongoClient = connectMongo.connectMongoClientSSLToAtlasWithMinimalSecurity();
                MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD_VC_PROFILE");
                MongoCollection mongoCollection = mongoDatabase.getCollection("profile");
                Document filter = new Document("vcInfo.vcName", vc);
                List<Document> documentFH = documentVCFundingHistoryData(fhSorted);
                Document preparedFH = new Document("fundingHistory", documentFH);
                mongoCollection.updateOne(filter, new BasicDBObject("$set", new BasicDBObject(preparedFH)));
                mongoClient.close();
                System.out.println(vc + " is inserted");
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (connectMongo.mongoClient != null) {

                    connectMongo.mongoClient = null;
                }
            }
        }

    }

    public static List<Document> documentVCFundingHistoryData(List<FundingHistory> fh){
        List<Document> fundingHistoryData = new ArrayList<>();
        Document document = null;
        for(FundingHistory itr:fh) {
            document = new Document().append(vcFields.fundingDate, itr.getFundingDate()).append(vcFields.companyName,
                    itr.getCompanyName()).append(vcFields.fundingAmount,itr.getFundingAmount()).append(vcFields.fundingRound,
                    itr.getFundingRound()).append(vcFields.categories, itr.getCategories());

            fundingHistoryData.add(document);
        }

        return fundingHistoryData;
    }
}
