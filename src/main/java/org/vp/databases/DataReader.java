package org.vp.databases;
import java.io.IOException;
import java.net.URL;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

/**
 * Created by mrahman on 12/16/16.
 */
public class DataReader {

    public static final String GOOGLE_ACCOUNT_USERNAME = "venturepulse@gmail.com"; // Fill in google account username
    public static final String GOOGLE_ACCOUNT_PASSWORD = "kmhr1234"; // Fill in google account password
    public static final String SPREADSHEET_URL = "https://docs.google.com/spreadsheets/d/1y_W3qd0YaFw6HdI0udBa-kU4Lrd2WgTIKZlvE3dBsPU/edit#gid=0"; //Fill in google spreadsheet URI

    public static void main(String[] args) throws IOException, ServiceException
    {
        /** Our view of Google Spreadsheets as an authenticated Google user. */
        SpreadsheetService service = new SpreadsheetService("Print Google Spreadsheet Demo");
        System.out.println("test");
        // Login and prompt the user to pick a sheet to use.
        service.setUserCredentials(GOOGLE_ACCOUNT_USERNAME, GOOGLE_ACCOUNT_PASSWORD);

        // Load sheet
        URL metafeedUrl = new URL(SPREADSHEET_URL);
        SpreadsheetEntry spreadsheet = service.getEntry(metafeedUrl, SpreadsheetEntry.class);
        URL listFeedUrl = ((WorksheetEntry) spreadsheet.getWorksheets().get(0)).getListFeedUrl();

        // Print entries
        ListFeed feed = (ListFeed) service.getFeed(listFeedUrl, ListFeed.class);
        for(ListEntry entry : feed.getEntries())
        {
            System.out.println("new row");
            for(String tag : entry.getCustomElements().getTags())
            {
                System.out.println("     "+tag + ": " + entry.getCustomElements().getValue(tag));
            }
        }
    }
}
