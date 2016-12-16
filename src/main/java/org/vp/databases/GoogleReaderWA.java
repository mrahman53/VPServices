package org.vp.databases;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by mrahman on 12/16/16.
 */
public class GoogleReaderWA {

    public static void main(String[] args) {
        SpreadsheetService service = new SpreadsheetService("Corporate VC");
        // https://spreadsheets/d/1y_W3qd0YaFw6HdI0udBa-kU4Lrd2WgTIKZlvE3dBsPU/pubhtml
        try {
            String sheetUrl = "https://spreadsheets.google.com/feeds/list/1y_W3qd0YaFw6HdI0udBa-kU4Lrd2WgTIKZlvE3dBsPU/default/public/values";

            // Use this String as url
            URL url = new URL(sheetUrl);

            // Get Feed of Spreadsheet url
            ListFeed lf = service.getFeed(url, ListFeed.class);

            //Iterate over feed to get cell value
            for (ListEntry le : lf.getEntries()) {
                CustomElementCollection cec = le.getCustomElements();
                //Pass column name to access it's cell values
                String val = cec.getValue("vcName");
                System.out.println(val);
                String val2 = cec.getValue("vcType");
                System.out.println(val2);
                String val3 = cec.getValue("vcLocation");
                String [] location = val3.split(",");
                System.out.println(location[0]);
                System.out.println(location[1]);
                System.out.println(location[2]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
