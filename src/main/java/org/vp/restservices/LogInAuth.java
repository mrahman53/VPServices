package org.vp.restservices;

import org.vp.authentication.AdminUserProfile;
import org.vp.authentication.AdminUserOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by mrahman on 8/24/16.
 */

@Path("LogInAuth")
//@Consumes(MediaType.APPLICATION_JSON)
//@Produces(MediaType.APPLICATION_JSON)
public class LogInAuth {

        AdminUserOperation adminUserOperation = new AdminUserOperation();

        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public boolean postIt(AdminUserProfile user) {
            return adminUserOperation.verifyUser(user);
        }
        @PUT
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public String updateProfile(AdminUserProfile user) {
            return adminUserOperation.updateUserProfileSetting(user);
        }

        /*
        @GET
        @Path("/{profileId}")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public boolean loginProfile(@PathParam("profileId")String profile){
            //return adminUserOperation.getUserProfile(profileId);
            return adminUserOperation.verifyUser(profile);
        } */

}
