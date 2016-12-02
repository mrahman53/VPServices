package org.vp.authentication;

import org.vp.databases.ClientUserDatabaseServices;

/**
 * Created by mrahman on 2/12/16.
 */
public class ClientUserOperation {

    ClientUserDatabaseServices clientUserDatabaseServices = new ClientUserDatabaseServices();

    public ClientUserProfile getUserProfile(String email){
       ClientUserProfile clientUserProfile =  clientUserDatabaseServices.login(email);

        return clientUserProfile;
    }

    public String registerProfile(ClientUserProfile clientUserProfile){
       String message = clientUserDatabaseServices.adminRegistration(clientUserProfile);
        return message;
    }

    public String updateUserProfileSetting(ClientUserProfile clientUserProfile){
        String message = clientUserDatabaseServices.updateAdminUserProfile(clientUserProfile);

        return message;
    }
}
