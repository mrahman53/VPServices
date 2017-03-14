package org.vp.restservices;

import org.vp.databases.GoogleSheetIntegration;
import org.vp.databases.VCDatabaseServices;
import org.vp.vc.profile.VCProfile;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by mrahman on 3/14/17.
 */


@Path("FundingHistoryResources")
public class FundingHistoryResources {

        GoogleSheetIntegration googleSheetIntegration = new GoogleSheetIntegration();

        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public VCProfile getACompany()throws IOException,KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
            return googleSheetIntegration.getGoogleData();
        }

}
