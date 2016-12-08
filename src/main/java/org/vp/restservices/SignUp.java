package org.vp.restservices;

import org.vp.authentication.AdminUserOperation;
import org.vp.authentication.AdminUserProfile;
import org.vp.authentication.ClientUserProfile;
import org.vp.authentication.ClientUserOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mrahman on 2/12/16.
 */

@Path("SignUp")
//@Consumes(MediaType.APPLICATION_JSON)
//@Produces(MediaType.APPLICATION_JSON)
public class SignUp {

    AdminUserOperation adminUserOperation = new AdminUserOperation();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean postIt(AdminUserProfile adminUserProfile)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return adminUserOperation.registerProfile(adminUserProfile);
    }

    @PUT
    public String updateProfile(AdminUserProfile adminUserProfile) {
        return adminUserOperation.updateUserProfileSetting(adminUserProfile);
    }


    @GET
    @Path("/{profileId}")
    public AdminUserProfile loginProfile(@PathParam("profileId")String profileId)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return adminUserOperation.getUserProfile(profileId);
    }

}
