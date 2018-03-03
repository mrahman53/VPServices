package org.vp.restservices;

import org.vp.databases.VCDatabaseServices;
import org.vp.vc.profile.VCProfile;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Path("secured")
public class UnsortedAllVcResources {
    VCDatabaseServices vcDatabaseServices = new VCDatabaseServices();
    @GET
    @Path("UnsortedAllVcResources")
    @Produces(MediaType.APPLICATION_JSON)
    public List<VCProfile> getCompanyList()throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return vcDatabaseServices.queryUnsortedListOfCompany();
    }
}

