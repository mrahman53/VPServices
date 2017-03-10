package org.vp.restservices;

import  org.vp.databases.VCDatabaseServices;
import  org.vp.vc.profile.VCProfile;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by mrahman on 9/15/16.
 */

@Path("AllVcResources")
public class AllVcResources {

    VCDatabaseServices vcDatabaseServices = new VCDatabaseServices();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<VCProfile> getCompanyList(@QueryParam("start")int start, @QueryParam("size")int size)throws
            KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return vcDatabaseServices.queryListOfCompany();
    }

    //@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<VCProfile> getACompanies()throws
            KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return vcDatabaseServices.queryListOfCompanyByPagination(0,199);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) //MediaType.APPLICATION_FORM_URLENCODED
    public boolean postOrganizationProfile(VCProfile profile)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException  {
        System.out.println("POST Request has come to post to Insert a vc profile");
        boolean postMessage = vcDatabaseServices.insertVCProfileNReturn(profile);
        return postMessage;

    }

}
