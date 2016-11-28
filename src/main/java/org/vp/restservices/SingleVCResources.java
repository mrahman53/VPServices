package org.vp.restservices;

import org.glassfish.jersey.server.JSONP;
import org.vp.databases.VCDatabaseServices;
import org.vp.vc.profile.VCProfile;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by mrahman on 7/17/16.
 */

@Path("SingleVcResources")
public class SingleVCResources {

    VCDatabaseServices vcDatabaseServices = new VCDatabaseServices();

    @GET
    @Path("/{vcId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<VCProfile> getACompany(@PathParam("vcId") String vcId){

        System.out.println("GET Request has come to get "+vcId+" profile");
        //return vcDatabaseServices.findOneVCProfile(vcId);
        return vcDatabaseServices.queryListOfCompany(vcId);
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
