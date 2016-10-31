package org.vp.authentication;

import org.vp.databases.ConnectDB;
import org.vp.databases.UserDatabaseServices;
import org.vp.authentication.User;

/**
 * Created by mrahman on 2/12/16.
 */
public class UserProfile {

    UserDatabaseServices userDatabaseServices = new UserDatabaseServices();

    public User getUserProfile(String email){
       User user =  userDatabaseServices.login(email);

        return user;
    }

    public String registerProfile(User user){
       String message = userDatabaseServices.adminRegistration(user);
        return message;
    }

    public String updateUserProfileSetting(User user){
        String message = userDatabaseServices.updateAdminUserProfile(user);

        return message;
    }
}
