package org.vp.restservices;

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
 * Created by mrahman on 7/17/16.
 */

@Path("SingleVcResources")
public class SingleVCResources {

    VCDatabaseServices vcDatabaseServices = new VCDatabaseServices();

    @GET
    @Path("/{vcId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<VCProfile> getACompany(@PathParam("vcId") String vcId)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        //return vcDatabaseServices.findOneVCProfile(vcId);
        return vcDatabaseServices.queryListOfCompany(vcId);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) //MediaType.APPLICATION_FORM_URLENCODED
    public boolean postOrganizationProfile(VCProfile profile) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        //String postMessage = vcDatabaseServices.insertVCProfile(profile);
        boolean postMessage = vcDatabaseServices.insertVCProfileNReturn(profile);
        return postMessage;
    }

    @PUT
    @Path("/{vcId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) //MediaType.APPLICATION_FORM_URLENCODED
    public boolean updateOrganizationProfile(VCProfile profile) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        //String postMessage = vcDatabaseServices.insertVCProfile(profile);
        boolean postMessage = vcDatabaseServices.updateVCProfileNReturn(profile);
        return postMessage;
    }

    @DELETE
    @Path("/{vcId}")
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) //MediaType.APPLICATION_FORM_URLENCODED
    public boolean deleteOrganizationProfile(@PathParam("vcId") String profile) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        //String postMessage = vcDatabaseServices.insertVCProfile(profile);
        boolean postMessage = vcDatabaseServices.deleteVCProfileNReturn(profile);
        return postMessage;

    }
}
