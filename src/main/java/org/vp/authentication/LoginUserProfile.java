package org.vp.authentication;

import org.vp.databases.LoginDatabaseServices;
import org.vp.databases.UserDatabaseServices;

/**
 * Created by mrahman on 8/24/16.
 */
public class LoginUserProfile {
    LoginDatabaseServices loginDatabaseServices = new LoginDatabaseServices();

    public LoginUser getUserProfile(String email){
        LoginUser user =  loginDatabaseServices.login(email);

        return user;
    }
    public LoginUser verifyUserProfile(LoginUser user){
        LoginUser loginUser = loginDatabaseServices.login(user.getUsername());
        verifyUser(user);
        return loginUser;
    }
    public LoginUser verifyValidUser(LoginUser user){
        LoginUser loginUser = loginDatabaseServices.login(user.getUsername());

        if(loginUser.username.equals(user.username)) {

            if (loginUser.password.equals(user.password)) {

                return loginUser;

            }else{
                return null;
            }
        }else{

            return null;
        }
    }
    public boolean verifyUser(LoginUser user){
        boolean loginUser = loginDatabaseServices.loginVerify(user.getUsername(), user.getPassword());

            return loginUser;
    }

    public String registerProfile(LoginUser user){
        String message = loginDatabaseServices.adminRegistration(user);
        return message;
    }

    public String updateUserProfileSetting(LoginUser user){
        String message = loginDatabaseServices.updateAdminUserProfile(user);

        return message;
    }
}
