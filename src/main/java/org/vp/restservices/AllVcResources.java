package org.vp.restservices;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import  org.vp.databases.VCDatabaseServices;
import  org.vp.vc.profile.VCProfile;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    public List<VCProfile> getACompany()throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return vcDatabaseServices.queryListOfCompany();
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
