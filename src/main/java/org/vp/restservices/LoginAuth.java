package org.vp.restservices;

import org.vp.authentication.LoginUser;
import org.vp.authentication.LoginUserProfile;
import org.vp.authentication.User;
import org.vp.authentication.UserProfile;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by mrahman on 8/24/16.
 */

@Path("loginauth")
//@Consumes(MediaType.APPLICATION_JSON)
//@Produces(MediaType.APPLICATION_JSON)
public class LoginAuth {

        LoginUserProfile loginUserProfile = new LoginUserProfile();

        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public boolean postIt(LoginUser user) {
            return loginUserProfile.verifyUser(user);
        }
        @PUT
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public String updateProfile(LoginUser user) {
            return loginUserProfile.updateUserProfileSetting(user);
        }

       /*
        @GET
        //@Path("/{profileId}")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public boolean loginProfile(@PathParam("profileId")LoginUser profileId){
            //return loginUserProfile.getUserProfile(profileId);
            return loginUserProfile.verifyUser(profileId);
        }  */

}
