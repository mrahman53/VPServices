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
 * Created by mrahman on 10/30/17.
 */
@Path("UnSortedAllVcResources")
public class UnSortedAllVcResources {

        VCDatabaseServices vcDatabaseServices = new VCDatabaseServices();

        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public List<VCProfile> getCompanyList()throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
            return vcDatabaseServices.queryListOfUnSortedCompany();
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
