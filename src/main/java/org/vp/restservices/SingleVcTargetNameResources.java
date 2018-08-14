package org.vp.restservices;

import org.vp.databases.VCDatabaseServices;
import org.vp.vc.profile.VCProfile;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by mrahman on 7/6/18.
 */
@Path("secured/SingleVcTargetNameResources")
public class SingleVcTargetNameResources {

    VCDatabaseServices vcDatabaseServices = new VCDatabaseServices();

    @GET
    @Path("/{vcId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<VCProfile> getACompany(@PathParam("vcId") String vcId)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return vcDatabaseServices.queryListOfCompanyByID(vcId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean postOrganizationProfile(VCProfile profile) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        boolean postMessage = vcDatabaseServices.insertVCProfileNReturn(profile);
        return postMessage;
    }

    @PUT
    @Path("/{vcId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateOrganizationProfile(VCProfile profile) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        boolean postMessage = vcDatabaseServices.updateVCProfileByIDNReturn(profile);
        return postMessage;
    }

    @DELETE
    @Path("/{vcId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteOrganizationProfile(@PathParam("vcId")String profile) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        //boolean postMessage =
        vcDatabaseServices.deleteVCProfileByIDNReturn(profile);
        //return postMessage;

    }
}
