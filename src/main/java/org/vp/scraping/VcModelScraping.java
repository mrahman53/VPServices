package org.vp.scraping;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;
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
 * Created by mrahman on 2/18/17.
 */

public class VcModelScraping {

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
                VcModelScraping.class.getResourceAsStream("/client_secret.json");
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

    public static void main(String[] args) throws IOException {
        ConnectMongo connectMongo = new ConnectMongo();
        VCProfile vcProfile = null;
        VCInfo vcInfo = new VCInfo();
        Location location = new Location();
        SocialData socialData = new SocialData();
        List<FundingHistory> fh = new ArrayList<FundingHistory>();
        List<VCProfile> listProfile = new ArrayList<VCProfile>();
        List<Document> documentProfile = new ArrayList<Document>();
        // Build a new authorized API client service.
        Sheets service = getSheetsService();
        //1l2Xp4_SP2yNynVAs86wWrlkmaZt0gOtU7G8fmP_cO68
        // Prints the names and majors of students in a sample spreadsheet:
        // https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
        String spreadsheetId = "1it6e1F8oFQpH6Thz6Bf9k32QtzlO6WuAPzF1n25DCNM";
        String range = "Private Equity Firm & Fund Of Fund";
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.size() == 0) {
            System.out.println("No data found.");
        } else {
                for (List row : values) {
                    String vcName = (String) row.get(0).toString();
                    String vcType = (String) row.get(1).toString();
                    String city = (String) row.get(2).toString();
                    String state = (String) row.get(3).toString();
                    String country = (String) row.get(4).toString();
                    String numberOfDeals = (String) row.get(5).toString();
                    String numberOfExits = (String) row.get(6).toString();
                    String vcUrl = (String) row.get(7).toString();
                    String vcEmail = (String) row.get(8).toString();
                    String vcFoundedYear = (String) row.get(9).toString();
                    String facebookUrl = (String) row.get(10).toString();
                    String twitterUrl = (String) row.get(11).toString();
                    String linkedInUrl = (String) row.get(12).toString();
                    vcInfo.setVcName(vcName);
                    vcInfo.setVcType(vcType);
                    location.setCity(city);
                    location.setState(state);
                    location.setCountry(country);
                    vcInfo.setNumberOfDeals(numberOfDeals);
                    vcInfo.setNumberOfExits(numberOfExits);
                    vcInfo.setVcUrl(vcUrl);
                    vcInfo.setVcEmail(vcEmail);
                    vcInfo.setVcFoundedYear(vcFoundedYear);

                    socialData.setFacebookUrl(facebookUrl);
                    socialData.setTwitterUrl(twitterUrl);
                    socialData.setLinkedinUrl(linkedInUrl);

                    location = new Location(location.getCity(),location.getState(),location.getCountry());
                    vcInfo = new VCInfo(vcInfo.getVcName(),vcInfo.getVcType(),location,
                    vcInfo.getNumberOfDeals(),vcInfo.getNumberOfExits(),vcInfo.getVcUrl(),vcInfo.getVcEmail(),
                    vcInfo.getVcFoundedYear());
                    socialData = new SocialData(socialData.getFacebookUrl(),socialData.getTwitterUrl(),
                            socialData.getLinkedinUrl());
                    vcProfile = new VCProfile(vcInfo,socialData,fh);
                    listProfile.add(vcProfile);
                    location = new Location();
                    vcInfo = new VCInfo();
                    socialData = new SocialData();
                    vcProfile = new VCProfile();
                        // Print columns A and E, which correspond to indices 0 and 4.
                        System.out.printf("%s, %s,%s, %s,%s, %s,%s, %s,%s, %s,\n", row.get(0), row.get(1), row.get(2), row.get(3),
                                row.get(4), row.get(5), row.get(6), row.get(7), row.get(8), row.get(9));

            }
        }

        for(int i=1; i<listProfile.size(); i++) {
            Document vcInfoDocument = documentVCInfoDataDelta(listProfile.get(i));
            Document socialDataDocument = documentVCSocialData(listProfile.get(i));
            Document preparedDocument = new Document("vcInfo", vcInfoDocument).append("socialData", socialDataDocument);
            documentProfile.add(preparedDocument);
        }
        try{
        MongoClient mongoClient = connectMongo.connectMongoClientSSLToAtlasWithMinimalSecurity();
        MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD_VC_PROFILE");
        MongoCollection mongoCollection = mongoDatabase.getCollection("profile");
        mongoCollection.insertMany(documentProfile);
        mongoClient.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if (connectMongo.mongoClient != null) {

                connectMongo.mongoClient = null;
            }
        }

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