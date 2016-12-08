package org.vp.authentication;

import org.vp.databases.AdminUserDatabaseServices;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mrahman on 8/24/16.
 */
public class AdminUserOperation {
    AdminUserDatabaseServices adminUserDatabaseServices = new AdminUserDatabaseServices();

    public AdminUserProfile getUserProfile(String email){
        AdminUserProfile user =  adminUserDatabaseServices.login(email);

        return user;
    }
    public AdminUserProfile verifyUserProfile(AdminUserProfile user)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        AdminUserProfile adminUserProfile = adminUserDatabaseServices.login(user.getEmail());
        verifyUser(user);
        return adminUserProfile;
    }
    public AdminUserProfile verifyValidUser(AdminUserProfile user){
        AdminUserProfile adminUserProfile = adminUserDatabaseServices.login(user.getEmail());

        if(adminUserProfile.email.equals(user.getEmail())) {

            if (adminUserProfile.password.equals(user.password)) {

                return adminUserProfile;

            }else{
                return null;
            }
        }else{

            return null;
        }
    }
    public boolean verifyUser(AdminUserProfile user)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        boolean loginUser = adminUserDatabaseServices.loginVerify(user.getEmail(), user.getPassword());

            return loginUser;
    }

    public boolean registerProfile(AdminUserProfile user)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        boolean message = adminUserDatabaseServices.adminRegistration(user);
        return message;
    }


    public String updateUserProfileSetting(AdminUserProfile user){
        String message = adminUserDatabaseServices.updateAdminUserProfile(user);

        return message;
    }
}
