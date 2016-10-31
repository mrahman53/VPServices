package org.vp.restservices;

import  org.vp.databases.VCDatabaseServices;
import  org.vp.vc.profile.VCProfile;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by mrahman on 9/15/16.
 */

@Path("AllVcResources")
public class AllVcResources {

    VCDatabaseServices vcDatabaseServices = new VCDatabaseServices();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<VCProfile> getACompany(){

        return vcDatabaseServices.queryListOfCompany();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) //MediaType.APPLICATION_FORM_URLENCODED
    public boolean postOrganizationProfile(VCProfile profile) {
        System.out.println("POST Request has come to post to Insert a vc profile");
        //String postMessage = vcDatabaseServices.insertVCProfile(profile);
        boolean postMessage = vcDatabaseServices.insertVCProfileNReturn(profile);
        return postMessage;

    }

}
