package org.vp.scraping;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mrahman on 3/14/17.
 */
public class FundingHistoryScraping {
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
        //GoogleCredential credential = null;
        try {
            //credential = GoogleCredential.getApplicationDefault();
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

    public static void main(String[] args) throws IOException {
        ConnectMongo connectMongo = new ConnectMongo();
        VCProfile vcProfile = null;
        VCInfo vcInfo = new VCInfo();
        Location location = new Location();
        SocialData socialData = new SocialData();
        FundingHistory fundingHistory = new FundingHistory();
        List<FundingHistory> fh = new ArrayList<FundingHistory>();
        List<VCProfile> listProfile = new ArrayList<VCProfile>();
        List<Document> documentProfile = new ArrayList<Document>();
        // Build a new authorized API client service.
        Sheets service = getSheetsService();
        //1l2Xp4_SP2yNynVAs86wWrlkmaZt0gOtU7G8fmP_cO68
        // Prints the names and majors of students in a sample spreadsheet:
        // https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
        //1J9aiuz9f-DYDUimEPj5fJgpop0SiM2cfKAl7j6y-Qt4
        //1l_JyK4GxZBLZG8GGIJiuT0G3oapn33CQih5vYubgwo0
        String spreadsheetId = "1TD2v1jDXfV3Vikr8PAw7H7MLNPZWl5dhe1WiiafhV8g";//"1l_JyK4GxZBLZG8GGIJiuT0G3oapn33CQih5vYubgwo0";
        String vc = "500 Startups";
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, vc)
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
                //vcProfile = new VCProfile(fh);
                System.out.printf("%s, %s,%s, %s,%s,\n", row.get(0), row.get(1), row.get(2), row.get(3),
                        row.get(4));

            }
        }

        /*
        for(int i=1; i<listProfile.size(); i++) {
            Document vcInfoDocument = documentVCInfoDataDelta(listProfile.get(i));
            Document socialDataDocument = documentVCSocialData(listProfile.get(i));
            Document preparedDocument = new Document("vcInfo", vcInfoDocument).append("socialData", socialDataDocument);
            documentProfile.add(preparedDocument);
        } */
        try{
            MongoClient mongoClient = connectMongo.connectMongoClientSSLToAtlasWithMinimalSecurity();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD_VC_PROFILE");
            MongoCollection mongoCollection = mongoDatabase.getCollection("profile");
            Document filter = new Document("vcInfo.vcName", vc);
            List<Document> documentFH = documentVCFundingHistoryData(fh);
            Document preparedFH = new Document("fundingHistory",documentFH);
            mongoCollection.updateOne(filter,new BasicDBObject("$set",new BasicDBObject(preparedFH)));;
            mongoClient.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if (connectMongo.mongoClient != null) {

                connectMongo.mongoClient = null;
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

    public static Document documentVCInfoDataDelta(VCProfile profile){
        Document document = new Document().append(vcFields.vcName, profile.getVcInfo().getVcName())
                .append(vcFields.vcType, profile.getVcInfo().getVcType()).append(vcFields.vcLocation,vcLocationDocument(profile))
                .append(vcFields.numberOfDeals, profile.getVcInfo().getNumberOfDeals())
                .append(vcFields.numberOfExits, profile.getVcInfo().getNumberOfExits()).append(vcFields.vcUrl,
                        profile.getVcInfo().getVcUrl()).append(vcFields.vcEmail,profile.getVcInfo().getVcEmail())
                .append(vcFields.vcFoundedYear, profile.getVcInfo().getVcFoundedYear());

        return document;
    }

    public static Document vcLocationDocument(VCProfile profile){
        Document document = new Document().append(vcFields.city, profile.getVcInfo()
                .getVcLocation().getCity()).append(vcFields.state, profile.getVcInfo().getVcLocation().getState())
                .append(vcFields.country, profile.getVcInfo().getVcLocation().getCountry());

        return document;
    }

    public static Document documentVCSocialData(VCProfile profile){
        Document document = new Document().append(vcFields.facebookUrl, profile.getSocialData().getFacebookUrl())
                .append(vcFields.twitterUrl, profile.getSocialData().getTwitterUrl()).append(vcFields.linkedinUrl,
                        profile.getSocialData().getLinkedinUrl());

        return document;
    }

}
