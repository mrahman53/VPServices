package org.vp.databases;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.google.gdata.client.authn.oauth.*;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;


/**
 * Created by mrahman on 12/16/16.
 */
public class DataReader {

    public static final String GOOGLE_ACCOUNT_USERNAME = "venturepulse@gmail.com"; // Fill in google account username
    public static final String GOOGLE_ACCOUNT_PASSWORD = "kmhr1234"; // Fill in google account password
    public static final String SPREADSHEET_URL = "https://docs.google.com/spreadsheets/d/1y_W3qd0YaFw6HdI0udBa-kU4Lrd2WgTIKZlvE3dBsPU/edit#gid=1539086458"; //Fill in google spreadsheet URI

    public static void main(String[] args) throws IOException, ServiceException
    {
        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();

        // Set your OAuth Consumer Key (which you can register at
        // https://www.google.com/accounts/ManageDomains).
        oauthParameters.setOAuthConsumerKey("349706551789-bo99n3qn49gv8j11aqjleo3qb0bq4jan.apps.googleusercontent.com");

        // Initialize the OAuth Signer.
        OAuthSigner signer = null;
        oauthParameters.setOAuthConsumerSecret("OUySpYY5-Dkl4o8rVJllC1m6");
        signer = new OAuthHmacSha1Signer();

        // Finally create a new GoogleOAuthHelperObject.  This is the object you
        // will use for all OAuth-related interaction.
        GoogleOAuthHelper oauthHelper = new GoogleOAuthHelper(signer);

        oauthParameters.setScope(SPREADSHEET_URL);

        // This method also makes a request to get the unauthorized request token,
        // and adds it to the oauthParameters object, along with the token secret
        // (if it is present).
        try {
            oauthHelper.getUnauthorizedRequestToken(oauthParameters);
        } catch (OAuthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        URL feedUrl = null;
        try {
            feedUrl = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Sending request to " + feedUrl.toString());
        System.out.println();
        SpreadsheetService googleService =
                new SpreadsheetService("oauth-sample-app");

        // Set the OAuth credentials which were obtained from the step above.
        try {
            googleService.setOAuthCredentials(oauthParameters, signer);
        } catch (OAuthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SpreadsheetFeed feed = googleService.getFeed(feedUrl, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();

        System.out.println("Response Data:");
        System.out.println("=====================================================");
        if(spreadsheets != null) {

            // Iterate through all of the spreadsheets returned
            for (SpreadsheetEntry spreadsheet : spreadsheets) {
                //Print the name of each spreadshet
                System.out.println(spreadsheet.getTitle().getPlainText());
            }
        }
    }
}
