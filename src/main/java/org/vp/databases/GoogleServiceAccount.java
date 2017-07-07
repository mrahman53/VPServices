package org.vp.databases;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.docs.SpreadsheetEntry;
import com.google.gdata.util.ServiceException;
import com.google.api.services.plus.Plus.Builder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;

/**
 * Created by mrahman on 3/15/17.
 */
public class GoogleServiceAccount {

//    public void getGoogleServiceData()throws GeneralSecurityException,IOException,ServiceException{
//        final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//        final String KEY_FILE_LOCATION = "/path/to/your.p12";
//        final String SERVICE_ACCOUNT_EMAIL = "vpsheet@vpdata-152705.iam.gserviceaccount.com";
//        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        GoogleCredential credential = new GoogleCredential.Builder()
//                .setTransport(httpTransport)
//                .setJsonFactory(JSON_FACTORY)
//                .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
//                .setServiceAccountPrivateKeyFromP12File(new File(KEY_FILE_LOCATION))
//                .setServiceAccountUser("venturepulse@gmail.com")
//                .build();
//        SpreadsheetService s = null;//getSpreadSheetService();
//        String spreadsheetURL = "https://spreadsheets.google.com/feeds/spreadsheets/" + file.getId();
//        SpreadsheetEntry spreadsheet = s.getEntry(new URL(spreadsheetURL), SpreadsheetEntry.class);
//    }
//
//    public void credentialSetUp() {
//        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
//        Plus plus = new Plus.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
//                .setApplicationName("Google-PlusSample/1.0").build();
//    }


}
