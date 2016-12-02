package org.vp.restservices;

import org.vp.authentication.AdminUserOperation;
import org.vp.authentication.AdminUserProfile;
import org.vp.authentication.ClientUserProfile;
import org.vp.authentication.ClientUserOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by mrahman on 2/12/16.
 */

@Path("SignUp")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SignUp {

    AdminUserOperation adminUserOperation = new AdminUserOperation();

    @POST
    public String postIt(AdminUserProfile adminUserProfile) {
        return adminUserOperation.registerProfile(adminUserProfile);
    }
    @PUT
    public String updateProfile(AdminUserProfile adminUserProfile) {
        return adminUserOperation.updateUserProfileSetting(adminUserProfile);
    }


    @GET
    @Path("/{profileId}")
    public AdminUserProfile loginProfile(@PathParam("profileId")String profileId){
        return adminUserOperation.getUserProfile(profileId);
    }

}
