package org.vp.restservices;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import org.vp.databases.ReactiveConnectMongo;
import org.vp.databases.SubscriberHelpers;
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

@Path("secured")
public class AllVcResources {

    VCDatabaseServices vcDatabaseServices = new VCDatabaseServices();
    ReactiveConnectMongo reactiveConnectMongo = null;
    //@GET
    @Path("AllVcResources")
    @Produces(MediaType.APPLICATION_JSON)
    public List<VCProfile> getCompanyList()throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return vcDatabaseServices.queryListOfCompany();
    }

    @GET
    @Path("AllVcResources")
    @Produces(MediaType.APPLICATION_JSON)
    public void getACompanies()throws Throwable {
        reactiveConnectMongo = new ReactiveConnectMongo();
        reactiveConnectMongo.readProfileCollection();
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
