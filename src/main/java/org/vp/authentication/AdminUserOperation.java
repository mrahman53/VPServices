package org.vp.authentication;

import org.vp.databases.AdminUserDatabaseServices;

/**
 * Created by mrahman on 8/24/16.
 */
public class AdminUserOperation {
    AdminUserDatabaseServices adminUserDatabaseServices = new AdminUserDatabaseServices();

    public AdminUserProfile getUserProfile(String email){
        AdminUserProfile user =  adminUserDatabaseServices.login(email);

        return user;
    }
    public AdminUserProfile verifyUserProfile(AdminUserProfile user){
        AdminUserProfile adminUserProfile = adminUserDatabaseServices.login(user.getUsername());
        verifyUser(user);
        return adminUserProfile;
    }
    public AdminUserProfile verifyValidUser(AdminUserProfile user){
        AdminUserProfile adminUserProfile = adminUserDatabaseServices.login(user.getUsername());

        if(adminUserProfile.username.equals(user.username)) {

            if (adminUserProfile.password.equals(user.password)) {

                return adminUserProfile;

            }else{
                return null;
            }
        }else{

            return null;
        }
    }
    public boolean verifyUser(AdminUserProfile user){
        boolean loginUser = adminUserDatabaseServices.loginVerify(user.getUsername(), user.getPassword());

            return loginUser;
    }

    public String registerProfile(AdminUserProfile user){
        String message = adminUserDatabaseServices.adminRegistration(user);
        return message;
    }

    public String updateUserProfileSetting(AdminUserProfile user){
        String message = adminUserDatabaseServices.updateAdminUserProfile(user);

        return message;
    }
}
