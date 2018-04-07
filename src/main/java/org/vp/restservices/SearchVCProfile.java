package org.vp.restservices;

import org.vp.databases.VCDatabaseServices;
import org.vp.vc.profile.VCProfile;

import org.vp.databases.VCDatabaseServices;
import org.vp.vc.profile.VCProfile;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by mrahman on 3/9/17.
 */

@Path("secured/SearchVCProfile")
public class SearchVCProfile {

    VCDatabaseServices vcDatabaseServices = new VCDatabaseServices();

    @GET
    @Path("/{vcName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<VCProfile> getACompany(@PathParam("vcName") String vcName)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return vcDatabaseServices.queryListOfCompanyByName(vcName);
    }

}

